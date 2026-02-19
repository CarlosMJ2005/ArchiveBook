package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.errors.TokenExpiredException
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class LibraryRepositoryImpl(
    private val apiService: LibraryApiService,
    private val authManager: AuthManager
) : LibraryRepository {

    private var cachedBooks = mutableListOf<Book>()

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
        return try {
            Result.success(apiCall())
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.Unauthorized) {
                Result.failure(TokenExpiredException())
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBooks(): Result<List<Book>> {
        return safeApiCall {
            coroutineScope {
                val booksDeferred = async { apiService.fetchBooks() }
                val loansDeferred = async { apiService.getLoans() }

                val booksResponse = booksDeferred.await()
                val loansResponse = loansDeferred.await()

                val userEmail = authManager.getEmail()

                // Create a set of book IDs that the current user needs to return.
                val pendingReturnIds = loansResponse
                    .filter { it.usuario?.email == userEmail && !it.devuelto }
                    .map { it.libro.id } // Corrected from `id` to `idLibro`
                    .toSet()

                val books = booksResponse.map { bookDto ->
                    // Correctly check against the DTO's `idLibro`
                    val isToReturn = pendingReturnIds.contains(bookDto.id)
                    bookDto.toDomain().copy(isToReturn = isToReturn)
                }

                cachedBooks.clear()
                cachedBooks.addAll(books)
                books
            }
        }
    }

    override suspend fun getBookById(id: Int): Result<Book?> {
        return Result.success(cachedBooks.find { it.id == id })
    }

    override suspend fun getCachedBookById(id: Int): Result<Book?> {
        return Result.success(cachedBooks.find { it.id == id })
    }

    override suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit> {
        return safeApiCall {
            if (isCurrentlyFavorite) apiService.removeFromFavorites(bookId)
            else apiService.addToFavorites(bookId)

            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index != -1) {
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isFavorite = !isCurrentlyFavorite)
            }
        }
    }

    override suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit> {
        return safeApiCall {
            if (isCurrentlyBookmarked) apiService.removeBookmark(bookId)
            else apiService.addBookmark(bookId)

            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index != -1) {
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isBookmarked = !isCurrentlyBookmarked)
            }
        }
    }

    override suspend fun toggleReturn(bookId: Int, isCurrentlyToReturn: Boolean): Result<Unit> {
        return safeApiCall {
            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index == -1) throw Exception("Libro no encontrado en caché")

            val success = if (isCurrentlyToReturn) {
                apiService.cancelReturn(bookId)
            } else {
                apiService.markToReturn(bookId)
            }

            if (success) {
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isToReturn = !isCurrentlyToReturn)
            } else {
                throw Exception("El servidor devolvió un error (Posible 404: ID no encontrado o ruta incorrecta)")
            }
        }
    }

    override fun clearCache() {
        cachedBooks.clear()
    }
}

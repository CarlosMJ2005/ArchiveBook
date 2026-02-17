package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.errors.TokenExpiredException
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

class LibraryRepositoryImpl(
    private val apiService: LibraryApiService
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
            if (cachedBooks.isNotEmpty()) {
                return@safeApiCall cachedBooks
            }
            val response = apiService.fetchBooks()
            val books = response.map { it.toDomain() }
            cachedBooks.clear()
            cachedBooks.addAll(books)
            cachedBooks
        }
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
}
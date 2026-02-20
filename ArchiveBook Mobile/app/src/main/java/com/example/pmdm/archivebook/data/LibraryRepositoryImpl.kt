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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class LibraryRepositoryImpl(
    private val apiService: LibraryApiService,
    private val authManager: AuthManager
) : LibraryRepository {

    private val _booksFlow = MutableStateFlow<List<Book>>(emptyList())
    override val books: Flow<List<Book>> = _booksFlow.asStateFlow()

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
                // 1. Lanzamos todas las peticiones en paralelo
                val booksDef = async { apiService.fetchBooks() }
                val loansDef = async { apiService.getLoans() }
                val favsDef = async { apiService.getFavorites() }
                val marksDef = async { apiService.getBookmarks() }

                val booksRes = booksDef.await()
                val loansRes = loansDef.await()
                val favRes = favsDef.await()
                val markRes = marksDef.await()

                // 2. Procesamos Sets para búsqueda rápida O(1)
                val favoriteIds = favRes.map { it.libro.id }.toSet()
                val bookmarkIds = markRes.map { it.libro.id }.toSet()

                val userEmail = authManager.getEmail()

                // Identificamos qué libros tienes TÚ pendientes de devolver
                val myActiveLoans = loansRes
                    .filter { it.usuario?.email == userEmail && !it.devuelto }
                    .map { it.libro.id }.toSet()

                // Identificamos qué libros están prestados por OTROS (no disponibles)
                val loanedByOthers = loansRes
                    .filter { it.usuario?.email != userEmail && !it.devuelto }
                    .map { it.libro.id }.toSet()

                val mappedBooks = booksRes.map { dto ->
                    dto.toDomain().copy(
                        isFavorite = favoriteIds.contains(dto.id),
                        isBookmarked = bookmarkIds.contains(dto.id),
                        isToReturn = myActiveLoans.contains(dto.id),
                        // Si lo tiene otro, 'isLoaned' será true para deshabilitar el préstamo
                        isLoaned = loanedByOthers.contains(dto.id)
                    )
                }

                _booksFlow.value = mappedBooks
                mappedBooks
            }
        }
    }

    override suspend fun borrowBook(bookId: Int): Result<Unit> {
        return safeApiCall {
            val success = apiService.borrowBook(bookId)
            if (success) {
                // Actualizamos localmente: ahora es un libro a devolver
                updateCachedBook(bookId) { it.copy(isToReturn = true) }
            } else {
                throw Exception("No se pudo realizar el préstamo.")
            }
        }
    }

    override suspend fun returnBook(bookId: Int): Result<Unit> {
        return safeApiCall {
            val success = apiService.returnBook(bookId)
            if (success) {
                // Actualizamos localmente: ya no está pendiente de devolución
                updateCachedBook(bookId) { it.copy(isToReturn = false) }
            } else {
                throw Exception("No se pudo devolver el libro.")
            }
        }
    }

    // --- MÉTODOS DE SOPORTE Y CACHÉ ---

    override suspend fun getBookById(id: Int): Result<Book?> {
        val book = _booksFlow.value.find { it.id == id }
        return if (book != null) Result.success(book)
        else getBooks().map { list -> list.find { it.id == id } }
    }

    override fun observeBookById(id: Int): Flow<Book?> =
        books.map { list -> list.find { it.id == id } }

    override suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit> {
        return safeApiCall {
            val success = if (isCurrentlyFavorite) apiService.removeFromFavorites(bookId)
            else apiService.addToFavorites(bookId)

            if (success) updateCachedBook(bookId) { it.copy(isFavorite = !isCurrentlyFavorite) }
        }
    }

    override suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit> {
        return safeApiCall {
            val success = if (isCurrentlyBookmarked) apiService.removeBookmark(bookId)
            else apiService.addBookmark(bookId)

            if (success) updateCachedBook(bookId) { it.copy(isBookmarked = !isCurrentlyBookmarked) }
        }
    }

    private fun updateCachedBook(bookId: Int, updateAction: (Book) -> Book) {
        val currentList = _booksFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == bookId }
        if (index != -1) {
            currentList[index] = updateAction(currentList[index])
            _booksFlow.value = currentList
        }
    }

    override fun clearCache() { _booksFlow.value = emptyList() }

    // Eliminado deleteLoan por petición del usuario
}
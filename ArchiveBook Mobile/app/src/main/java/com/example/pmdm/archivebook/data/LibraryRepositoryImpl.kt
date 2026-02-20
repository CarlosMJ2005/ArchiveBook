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

    // Cambiamos la lista simple por un StateFlow para que sea reactivo
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
                val booksDef = async { apiService.fetchBooks() }
                val loansDef = async { apiService.getLoans() }
                val favsDef = async { apiService.getFavorites() }
                val marksDef = async { apiService.getBookmarks() }

                val booksRes = booksDef.await()
                val loansRes = loansDef.await()

                val favoriteIds = favsDef.await().map { it.libro.id }.toSet()
                val bookmarkIds = marksDef.await().map { it.libro.id }.toSet()

                val userEmail = authManager.getEmail()
                val pendingReturnIds = loansRes.filter { it.usuario?.email == userEmail && !it.devuelto }
                    .map { it.libro.id }.toSet()
                val loanedBookIds = loansRes.filter { !it.devuelto }.map { it.libro.id }.toSet()

                val mappedBooks = booksRes.map { dto ->
                    dto.toDomain().copy(
                        isToReturn = pendingReturnIds.contains(dto.id),
                        //isLoaned = loanedBookIds.contains(dto.id),
                        isFavorite = favoriteIds.contains(dto.id),
                        isBookmarked = bookmarkIds.contains(dto.id)
                    )
                }

                // Actualizamos el StateFlow. Esto notificará a todos los observadores.
                _booksFlow.value = mappedBooks

                mappedBooks
            }
        }
    }

    override suspend fun getBookById(id: Int): Result<Book?> {
        // Buscamos en el valor actual del Flow
        val book = _booksFlow.value.find { it.id == id }

        return if (book != null) {
            Result.success(book)
        } else {
            // Si no está, forzamos una recarga
            val refreshResult = getBooks()
            if (refreshResult.isSuccess) {
                Result.success(_booksFlow.value.find { it.id == id })
            } else {
                Result.success(null)
            }
        }
    }

    // Método para observar un libro específico como Flow (ideal para la pantalla detalle)
    override fun observeBookById(id: Int): Flow<Book?> {
        return books.map { list -> list.find { it.id == id } }
    }

    override suspend fun getCachedBookById(id: Int): Result<Book?> {
        return Result.success(_booksFlow.value.find { it.id == id })
    }

    override suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit> {
        return safeApiCall {
            if (isCurrentlyFavorite) apiService.removeFromFavorites(bookId)
            else apiService.addToFavorites(bookId)

            updateCachedBook(bookId) { it.copy(isFavorite = !isCurrentlyFavorite) }
        }
    }

    override suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit> {
        return safeApiCall {
            if (isCurrentlyBookmarked) apiService.removeBookmark(bookId)
            else apiService.addBookmark(bookId)

            updateCachedBook(bookId) { it.copy(isBookmarked = !isCurrentlyBookmarked) }
        }
    }

    override suspend fun borrowBook(bookId: Int): Result<Unit> {
        return safeApiCall {
            val success = apiService.borrowBook(bookId)
            if (success) {
                updateCachedBook(bookId) { it.copy(isToReturn = true) }
            } else {
                throw Exception("Error al prestar el libro.")
            }
        }
    }

    override suspend fun returnBook(bookId: Int): Result<Unit> {
        return safeApiCall {
            val success = apiService.returnBook(bookId)
            if (success) {
                updateCachedBook(bookId) { it.copy(isToReturn = false) }
            } else {
                throw Exception("Error al devolver el libro.")
            }
        }
    }

    override suspend fun deleteLoan(bookId: Int): Result<Unit> {
        return safeApiCall {
            val success = apiService.deleteLoan(bookId)
            if (success) {
                updateCachedBook(bookId) { it.copy(isToReturn = false) }
            } else {
                throw Exception("Error al eliminar el préstamo.")
            }
        }
    }

    private fun updateCachedBook(bookId: Int, updateAction: (Book) -> Book) {
        val currentList = _booksFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == bookId }
        if (index != -1) {
            currentList[index] = updateAction(currentList[index])
            // Emitimos la nueva lista completa
            _booksFlow.value = currentList
        }
    }

    override fun clearCache() {
        _booksFlow.value = emptyList()
    }
}

package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository

class LibraryRepositoryImpl(
    private val apiService: LibraryApiService
) : LibraryRepository {

    // Caché en memoria para que toda la app vea los mismos datos
    private var cachedBooks = mutableListOf<Book>()

    override suspend fun getBooks(): Result<List<Book>> {
        return try {
            // Si ya tenemos libros, devolvemos la caché (o podrías decidir refrescar)
            if (cachedBooks.isNotEmpty()) {
                return Result.success(cachedBooks)
            }

            val response = apiService.fetchBooks()
            val books = response.map { it.toDomain() }

            cachedBooks.clear()
            cachedBooks.addAll(books)

            Result.success(cachedBooks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit> {
        return try {
            // 1. Llamada a la API
            if (isCurrentlyFavorite) apiService.removeFromFavorites(bookId)
            else apiService.addToFavorites(bookId)

            // 2. ACTUALIZAR CACHÉ LOCAL inmediatamente
            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index != -1) {
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isFavorite = !isCurrentlyFavorite)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit> {
        return try {
            if (isCurrentlyBookmarked) apiService.removeBookmark(bookId)
            else apiService.addBookmark(bookId)

            // ACTUALIZAR CACHÉ LOCAL
            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index != -1) {
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isBookmarked = !isCurrentlyBookmarked)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleReturn(bookId: Int, isCurrentlyToReturn: Boolean): Result<Unit> {
        return try {
            // 1. Buscamos en caché para validar que el libro existe localmente
            val index = cachedBooks.indexOfFirst { it.id == bookId }
            if (index == -1) return Result.failure(Exception("Libro no encontrado en caché"))

            // 2. Llamada a la API
            // IMPORTANTE: Ya no pasamos 'updatedBook.toDto()' porque tu API solo pide el ID
            val success = if (isCurrentlyToReturn) {
                apiService.cancelReturn(bookId)
            } else {
                apiService.markToReturn(bookId)
            }

            if (success) {
                // 3. ACTUALIZACIÓN REACTIVA: Solo actualizamos la caché si la API respondió OK
                val book = cachedBooks[index]
                cachedBooks[index] = book.copy(isToReturn = !isCurrentlyToReturn)

                Result.success(Unit)
            } else {
                // Si llega aquí con un 404, el problema suele ser que el bookId no existe en el server
                Result.failure(Exception("El servidor devolvió un error (Posible 404: ID no encontrado o ruta incorrecta)"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
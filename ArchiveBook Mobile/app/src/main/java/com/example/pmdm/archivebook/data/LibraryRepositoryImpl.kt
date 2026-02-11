package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository

class LibraryRepositoryImpl(
    private val apiService: LibraryApiService
) : LibraryRepository {

    override suspend fun getBooks(): Result<List<Book>> {
        return try {
            val response = apiService.fetchBooks()
            // Transformamos cada BookDto en un Book de dominio
            val books = response.map { it.toDomain() }
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit> {
        return try {
            if (isCurrentlyFavorite) {
                // Usamos 'apiService', que es como lo inyectamos en el constructor
                apiService.removeFromFavorites(bookId)
            } else {
                apiService.addToFavorites(bookId)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit> {
        return try {
            if (isCurrentlyBookmarked) {
                apiService.removeBookmark(bookId)
            } else {
                apiService.addBookmark(bookId)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleReturn(
        bookId: Int,
        isCurrentlyToReturn: Boolean
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}
package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.domain.Book
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    val books: Flow<List<Book>>
    suspend fun getBooks(): Result<List<Book>>
    suspend fun getBookById(id: Int): Result<Book?>
    fun observeBookById(id: Int): Flow<Book?>

    // Eliminado getToken y deleteLoan
    suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit>
    suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit>
    suspend fun borrowBook(bookId: Int): Result<Unit>
    suspend fun returnBook(bookId: Int): Result<Unit>
    fun clearCache()
}
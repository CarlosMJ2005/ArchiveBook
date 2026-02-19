package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.domain.Book
import kotlinx.coroutines.flow.Flow

interface LibraryRepository {
    // Añade esta línea para que el override del Repo funcione
    val books: Flow<List<Book>>

    suspend fun getBooks(): Result<List<Book>>
    suspend fun getBookById(id: Int): Result<Book?>

    // Añade esta línea también
    fun observeBookById(id: Int): Flow<Book?>

    suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit>
    suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit>
    suspend fun borrowBook(bookId: Int): Result<Unit>
    suspend fun returnBook(bookId: Int): Result<Unit>
    suspend fun deleteLoan(bookId: Int): Result<Unit>
    suspend fun getCachedBookById(id: Int): Result<Book?>
    fun clearCache()
}
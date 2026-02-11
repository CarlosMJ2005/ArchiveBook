package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.domain.Book

interface LibraryRepository {
    suspend fun getBooks(): Result<List<Book>>
    suspend fun toggleFavorite(bookId: Int, isCurrentlyFavorite: Boolean): Result<Unit>
    suspend fun toggleBookmark(bookId: Int, isCurrentlyBookmarked: Boolean): Result<Unit>
    suspend fun toggleReturn(bookId: Int, isCurrentlyToReturn: Boolean): Result<Unit>
}
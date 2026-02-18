package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.domain.Book
import java.util.NoSuchElementException

interface BookDetailRepository {
    suspend fun getBookById(bookId: Int): Result<Book>
}

class BookDetailRepositoryImpl(
    private val libraryRepository: LibraryRepository
) : BookDetailRepository {
    override suspend fun getBookById(bookId: Int): Result<Book> {
        val result = libraryRepository.getBookById(bookId)
        val book = result.getOrNull()
        return when {
            result.isSuccess && book != null -> Result.success(book)
            result.isSuccess && book == null -> Result.failure(NoSuchElementException("Book with id $bookId not found."))
            else -> Result.failure(result.exceptionOrNull()!!)
        }
    }
}
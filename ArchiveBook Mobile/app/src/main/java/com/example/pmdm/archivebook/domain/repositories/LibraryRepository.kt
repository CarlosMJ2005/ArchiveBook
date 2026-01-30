package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.domain.Book

interface LibraryRepository {
    suspend fun getBooks(): Result<List<Book>>
}
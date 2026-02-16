package com.example.pmdm.archivebook.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import com.example.pmdm.archivebook.presentation.BookDetailViewModel

class BookDetailViewModelFactory(
    private val repository: LibraryRepository,
    private val bookId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookDetailViewModel(bookId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
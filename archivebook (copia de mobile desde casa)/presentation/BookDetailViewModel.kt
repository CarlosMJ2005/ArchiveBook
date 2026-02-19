package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookId: Int,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    var book by mutableStateOf<Book?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadBook()
    }

    fun loadBook() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val result = libraryRepository.getBookById(bookId)
                result.onSuccess { fetchedBook ->
                    book = fetchedBook
                }.onFailure { error ->
                    errorMessage = error.message
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }
}

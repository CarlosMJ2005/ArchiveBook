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
    private val repository: LibraryRepository
) : ViewModel() {

    var book by mutableStateOf<Book?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadBook()
    }

    private fun loadBook() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            repository.getBooks()
                .onSuccess { allBooks ->
                    // Find the specific book in the list returned by your repository
                    book = allBooks.find { it.id == bookId }
                    if (book == null) {
                        errorMessage = "Book not found"
                    }
                }
                .onFailure {
                    errorMessage = it.message ?: "Error loading book"
                }

            isLoading = false
        }
    }
}
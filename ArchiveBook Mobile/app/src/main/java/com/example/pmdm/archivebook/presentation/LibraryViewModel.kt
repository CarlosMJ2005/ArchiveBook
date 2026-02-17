package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.errors.TokenExpiredException
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    private var allBooks by mutableStateOf<List<Book>>(emptyList())
    val state: List<Book> get() = filteredBooks

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var searchText by mutableStateOf("")
    var selectedFilter by mutableStateOf("Title")
    var selectedGenres = mutableStateListOf<String>()
        private set
    var forceLogout by mutableStateOf(false)

    val filteredBooks: List<Book>
        get() {
            return allBooks.filter { book ->
                when (selectedFilter) {
                    "Genre" -> selectedGenres.isEmpty() || book.genres.any { it in selectedGenres }
                    "Author" -> book.author.contains(searchText, ignoreCase = true)
                    "Publisher" -> book.publisher.contains(searchText, ignoreCase = true)
                    else -> book.title.contains(searchText, ignoreCase = true)
                }
            }
        }

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getBooks()
                .onSuccess { books ->
                    allBooks = books
                }
                .onFailure { e ->
                    if (e is TokenExpiredException) {
                        forceLogout = true
                    } else {
                        errorMessage = e.message ?: "Error desconocido"
                    }
                }
            isLoading = false
        }
    }

    fun toggleBestseller(bookId: Int) {
        allBooks = allBooks.map {
            if (it.id == bookId) it.copy(isBestseller = !it.isBestseller) else it
        }
    }

    fun toggleFavorite(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleFavorite(bookId, book.isFavorite).onFailure { e ->
                if (e is TokenExpiredException) forceLogout = true
                else errorMessage = e.message
            }
        }
    }

    fun toggleBookmark(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleBookmark(bookId, book.isBookmarked).onFailure { e ->
                if (e is TokenExpiredException) forceLogout = true
                else errorMessage = e.message
            }
        }
    }

    fun toggleReturn(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleReturn(bookId, book.isToReturn).onFailure { e ->
                if (e is TokenExpiredException) forceLogout = true
                else errorMessage = e.message
            }
        }
    }

    private fun refreshBooks() {
        viewModelScope.launch {
            repository.getBooks().onFailure { e ->
                if (e is TokenExpiredException) forceLogout = true
                else errorMessage = e.message
            }
        }
    }

    fun clearFilters() {
        searchText = ""
        selectedGenres.clear()
        selectedFilter = "Title"
    }

    fun clearFields() {
        searchText = ""
        selectedFilter = "Title"
        selectedGenres.clear()
    }

    fun toggleGenre(genre: String) {
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
            if (selectedGenres.isEmpty()) {
                selectedFilter = "Title"
            }
        } else {
            selectedGenres.add(genre)
        }
    }
}
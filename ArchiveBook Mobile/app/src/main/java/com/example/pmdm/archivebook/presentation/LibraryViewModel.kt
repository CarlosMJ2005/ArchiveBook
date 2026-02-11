package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository
) : ViewModel() {

    // Lista maestra de libros
    private var allBooks by mutableStateOf<List<Book>>(emptyList())
    val state: List<Book> get() = filteredBooks
    // Estados de UI
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var searchText by mutableStateOf("")
    var selectedFilter by mutableStateOf("Title")
    var selectedGenres by mutableStateOf<Set<String>>(emptySet())

    // Lógica de filtrado
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
                    allBooks = books // Esto disparará la recomposición de filteredBooks
                }
                .onFailure { e ->
                    errorMessage = e.message ?: "Error desconocido"
                }
            isLoading = false
        }
    }

    // --- FUNCIONES DE INTERACCIÓN (Toggles) ---

    fun toggleBestseller(bookId: Int) {
        allBooks = allBooks.map {
            if (it.id == bookId) it.copy(isBestseller = !it.isBestseller) else it
        }
    }

    /*fun toggleFavorite(bookId: Int) {
        allBooks = allBooks.map {
            if (it.id == bookId) it.copy(isFavorite = !it.isFavorite) else it
        }
    }*/

    /*fun toggleBookmark(bookId: Int) {
        allBooks = allBooks.map {
            if (it.id == bookId) it.copy(isBookmarked = !it.isBookmarked) else it
        }
    }*/

    fun toggleReturn(bookId: Int) {
        allBooks = allBooks.map {
            if (it.id == bookId) it.copy(isToReturn = !it.isToReturn) else it
        }
    }

    fun clearFilters() {
        searchText = ""
        selectedGenres = emptySet()
    }

    fun toggleGenre(genre: String) {
        selectedGenres = if (selectedGenres.contains(genre)) {
            selectedGenres - genre
        } else {
            selectedGenres + genre
        }
    }
}

package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
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

    fun toggleFavorite(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleFavorite(bookId, book.isFavorite).onSuccess {
                refreshBooks() // Refrescamos desde la fuente oficial
            }
        }
    }

    fun toggleBookmark(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleBookmark(bookId, book.isBookmarked).onSuccess {
                refreshBooks()
            }
        }
    }

    fun toggleReturn(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return

        viewModelScope.launch {
            repository.toggleReturn(bookId, book.isToReturn)
                .onSuccess {
                    // Opción A: Volver a cargar
                    loadBooks()
                    // Opción B: Si quieres velocidad, actualiza allBooks manualmente aquí
                }
                .onFailure { e ->
                    errorMessage = "Error: ${e.message}"
                }
        }
    }

    private fun refreshBooks() {
        viewModelScope.launch {
            repository.getBooks().onSuccess { updatedList ->
                // Simplemente actualizamos la lista maestra.
                // Como 'filteredBooks' depende de 'allBooks', se actualizará sola.
                allBooks = updatedList
            }.onFailure { e ->
                errorMessage = "Error al refrescar: ${e.message}"
            }
        }
    }

    fun clearFilters() {
        searchText = ""
        selectedGenres = emptySet()
    }

    fun clearFields() {
        searchText = ""
        selectedFilter = "Title"
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

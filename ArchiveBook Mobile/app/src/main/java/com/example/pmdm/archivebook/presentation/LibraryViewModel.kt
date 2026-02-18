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

    // --- ESTADOS ---
    // 1. Categoría (Drawer): All, Bestsellers, Favorites...
    var selectedCategory by mutableStateOf("All")

    // 2. Filtro de Búsqueda (Barra superior): Title, Author, Genre...
    var selectedFilter by mutableStateOf("Title")

    // 3. Texto escrito
    var searchText by mutableStateOf("")

    var selectedGenres = mutableStateListOf<String>()
        private set

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var forceLogout by mutableStateOf(false)

    // --- LÓGICA DE FILTRADO (AND) ---
    val filteredBooks: List<Book>
        get() {
            return allBooks.filter { book ->
                // 1. ¿Cumple la categoría del Drawer?
                val matchesCategory = when (selectedCategory) {
                    "Bestsellers" -> book.isBestseller
                    "Favorites"   -> book.isFavorite
                    "YetToRead"   -> book.isBookmarked
                    else          -> true // "All"
                }

                // 2. ¿Cumple el criterio de búsqueda superior?
                val matchesSearch = when (selectedFilter) {
                    "Genre"     -> selectedGenres.isEmpty() || book.genres.any { it in selectedGenres }
                    "Author"    -> book.author.contains(searchText, ignoreCase = true)
                    "Publisher" -> book.publisher.contains(searchText, ignoreCase = true)
                    // Title y cualquier otro caso
                    else        -> book.title.contains(searchText, ignoreCase = true)
                }

                matchesCategory && matchesSearch
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
                .onSuccess { books -> allBooks = books }
                .onFailure { e ->
                    if (e is TokenExpiredException) forceLogout = true
                    else errorMessage = e.message ?: "Error desconocido"
                }
            isLoading = false
        }
    }

    // --- ACCIONES DE LIMPIEZA ---
    fun clearFilters() {
        searchText = ""
        selectedGenres.clear()
        // ELIMINAMOS la línea: selectedFilter = "Title"
    }

    // Esta función es para cuando queremos volver a la vista inicial (All Library)
    fun resetToAll() {
        selectedCategory = "All"
        selectedFilter = "Title" // Aquí sí reseteamos el tipo de filtro
        searchText = ""
        selectedGenres.clear()
    }

    // --- TOGGLES Y ACCIONES DE LIBRO ---

    fun toggleGenre(genre: String) {
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
            if (selectedGenres.isEmpty()) selectedFilter = "Title"
        } else {
            selectedGenres.add(genre)
        }
    }

    fun toggleFavorite(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        val newStatus = !book.isFavorite
        viewModelScope.launch {
            repository.toggleFavorite(bookId, book.isFavorite).onSuccess {
                updateBookLocal(bookId) { it.copy(isFavorite = newStatus) }
            }.onFailure { handleFailure(it) }
        }
    }

    fun toggleBookmark(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        val newStatus = !book.isBookmarked
        viewModelScope.launch {
            repository.toggleBookmark(bookId, book.isBookmarked).onSuccess {
                updateBookLocal(bookId) { it.copy(isBookmarked = newStatus) }
            }.onFailure { handleFailure(it) }
        }
    }

    fun toggleReturn(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleReturn(bookId, book.isToReturn).onFailure { handleFailure(it) }
        }
    }

    // Si usas el botón de estrella en la Card:
    /*fun toggleBestseller(bookId: Int) {
        updateBookLocal(bookId) { it.copy(isBestseller = !it.isBestseller) }
    }*/

    // Helper para actualizar lista local limpiamente
    private fun updateBookLocal(bookId: Int, update: (Book) -> Book) {
        allBooks = allBooks.map { if (it.id == bookId) update(it) else it }
    }

    private fun handleFailure(e: Throwable) {
        if (e is TokenExpiredException) forceLogout = true
        else errorMessage = e.message
    }
}
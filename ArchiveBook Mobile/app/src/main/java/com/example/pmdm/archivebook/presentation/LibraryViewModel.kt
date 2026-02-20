package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.errors.TokenExpiredException
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: LibraryRepository,
    private val authManager: AuthManager // Añadimos esto
): ViewModel() {

    // Cambiado: Ahora allBooks se actualiza automáticamente desde el Repositorio
    private var allBooks by mutableStateOf<List<Book>>(emptyList())
    val token: String get() = authManager.getToken() ?: ""
    // --- ESTADOS DE UI ---
    var selectedCategory by mutableStateOf("All")
    var selectedFilter by mutableStateOf("Title")
    var searchText by mutableStateOf("")
    var selectedGenres = mutableStateListOf<String>()
        private set
    var userToken by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var forceLogout by mutableStateOf(false)

    // --- LÓGICA DE FILTRADO ---
    val filteredBooks: List<Book>
        get() {
            return allBooks.filter { book ->
                val matchesCategory = when (selectedCategory) {
                    "Bestsellers" -> book.isBestseller
                    "Favorites"   -> book.isFavorite
                    "YetToRead"   -> book.isBookmarked
                    "ToReturn"    -> book.isToReturn
                    else          -> true
                }

                val matchesSearch = when (selectedFilter) {
                    "Genre"     -> selectedGenres.isEmpty() || book.genres.any { it in selectedGenres }
                    "Author"    -> book.author.contains(searchText, ignoreCase = true)
                    "Publisher" -> book.publisher.contains(searchText, ignoreCase = true)
                    else        -> book.title.contains(searchText, ignoreCase = true)
                }

                matchesCategory && matchesSearch
            }
        }

    init {
        // Empezamos a escuchar al repositorio desde que nace el ViewModel
        observeRepository()
        // Cargamos los datos iniciales
        fetchAllBooks()
        loadToken()
    }

    private fun loadToken() {
        viewModelScope.launch {
            userToken = authManager.getToken() ?: ""
        }
    }

    private fun observeRepository() {
        viewModelScope.launch {
            repository.books.collect { updatedList ->
                allBooks = updatedList
            }
        }
    }

    fun fetchAllBooks() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            repository.getBooks()
                .onFailure { handleFailure(it) }
            isLoading = false
        }
    }

    // --- ACCIONES DE LIBRO ---
    // Nota: Ya no llamamos a updateBookState manualmente.
    // El repositorio emitirá el cambio y el "collect" de arriba actualizará la UI.

    fun toggleFavorite(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleFavorite(bookId, book.isFavorite)
                .onFailure { handleFailure(it) }
        }
    }

    fun toggleBookmark(bookId: Int) {
        val book = allBooks.find { it.id == bookId } ?: return
        viewModelScope.launch {
            repository.toggleBookmark(bookId, book.isBookmarked)
                .onFailure { handleFailure(it) }
        }
    }

    fun borrowBook(bookId: Int) {
        viewModelScope.launch {
            repository.borrowBook(bookId)
                .onFailure { handleFailure(it) }
        }
    }

    fun returnBook(bookId: Int) {
        viewModelScope.launch {
            repository.returnBook(bookId)
                .onFailure { handleFailure(it) }
        }
    }

    // --- GESTIÓN DE ERRORES ---

    private fun handleFailure(e: Throwable) {
        if (e is TokenExpiredException) {
            forceLogout = true
        } else {
            errorMessage = e.message ?: "Error desconocido"
        }
        isLoading = false
    }

    // --- GESTIÓN DE FILTROS ---

    fun toggleGenre(genre: String) {
        if (selectedGenres.contains(genre)) {
            selectedGenres.remove(genre)
            if (selectedGenres.isEmpty()) selectedFilter = "Title"
        } else {
            selectedGenres.add(genre)
        }
    }

    fun clearFilters() {
        searchText = ""
        selectedGenres.clear()
    }

    fun resetToAll() {
        selectedCategory = "All"
        selectedFilter = "Title"
        searchText = ""
        selectedGenres.clear()
    }
}
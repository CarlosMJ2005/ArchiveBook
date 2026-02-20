package com.example.pmdm.archivebook.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookDetailUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val token: String? = null
)

class BookDetailViewModel(
    private val bookId: Int,
    private val libraryRepository: LibraryRepository,
    private val token: String
): ViewModel() {

    private val _uiState = MutableStateFlow(BookDetailUiState(token = token))
    val uiState: StateFlow<BookDetailUiState> = _uiState.asStateFlow()
    init {
        loadAndObserveBook()
    }

    private fun loadAndObserveBook() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Forzamos que el repositorio cargue/refresque su caché interna
            libraryRepository.getBookById(bookId)

            // 2. IMPORTANTE: Nos suscribimos al Flow del repositorio.
            // Gracias a esto, cualquier cambio que ocurra en el repositorio
            // (favorito, reserva, etc.) hará que este bloque se ejecute solo.
            libraryRepository.observeBookById(bookId).collect { updatedBook ->
                if (updatedBook != null) {
                    _uiState.update {
                        it.copy(book = updatedBook, isLoading = false, error = null)
                    }
                } else {
                    // Si ya no está cargando y no hay libro, es un error
                    if (!_uiState.value.isLoading) {
                        _uiState.update {
                            it.copy(isLoading = false, error = "El libro con ID $bookId no existe.")
                        }
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        val currentBook = _uiState.value.book ?: return
        viewModelScope.launch {
            // Ya no hace falta actualizar el State local a mano con .update(...)
            // porque el 'collect' de arriba lo hará cuando el repo cambie.
            libraryRepository.toggleFavorite(bookId, currentBook.isFavorite)
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    fun toggleBookmark() {
        val currentBook = _uiState.value.book ?: return
        viewModelScope.launch {
            libraryRepository.toggleBookmark(bookId, currentBook.isBookmarked)
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    fun borrowBook() {
        viewModelScope.launch {
            libraryRepository.borrowBook(bookId).onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }

    fun returnBook() {
        viewModelScope.launch {
            libraryRepository.returnBook(bookId).onFailure { error ->
                _uiState.update { it.copy(error = error.message) }
            }
        }
    }
}
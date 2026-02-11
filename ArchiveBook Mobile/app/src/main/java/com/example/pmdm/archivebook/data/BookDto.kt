package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    val title: String,
    val synopsis: String,
    val author: String,
    val publisher: String,
    val genres: List<String>,
    val isBestseller: Boolean,
    // Añadimos estas dos para que la función toDomain las encuentre:
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false
)

// Ahora la función de mapeo ya no dará error
fun BookDto.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title,
        synopsis = this.synopsis, // Asegúrate de que el orden coincida con tu clase Book
        author = this.author,
        publisher = this.publisher,
        genres = this.genres,
        isBestseller = this.isBestseller,
        isFavorite = this.isFavorite,     // ¡Ahora sí existe!
        isBookmarked = this.isBookmarked, // ¡Ahora sí existe!
        isToReturn = this.isToReturn
    )
}
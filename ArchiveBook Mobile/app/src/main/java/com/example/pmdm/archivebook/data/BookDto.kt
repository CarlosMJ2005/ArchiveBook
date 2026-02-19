package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("idAutor") val id: Int? = null,
    @SerialName("nombre") val nombre: String,
    @SerialName("apellidos") val apellidos: String,
    @SerialName("nacionalidad") val nationality: String? = null
)

@Serializable
data class EditorialDto(
    @SerialName("idEditorial") val id: Int? = null,
    @SerialName("nombre") val nombre: String,
    @SerialName("direccion") val address: String? = null,
    @SerialName("pais") val country: String? = null,
    @SerialName("sitioWeb") val website: String? = null
)

@Serializable
data class BookDto(
    @SerialName("idLibro") val id: Int,
    @SerialName("titulo") val title: String,
    @SerialName("isbn") val isbn: String? = null,
    @SerialName("agnoPublicacion") val publicationYear: Int? = null,
    @SerialName("portadaLibro") val cover: String? = null,
    @SerialName("sinopsis") val synopsis: String?,
    @SerialName("autor") val authorObj: AuthorDto,
    @SerialName("editorial") val publisherObj: EditorialDto,
    @SerialName("categoria") val category: String,
    @SerialName("prestado") val isLoaned: Boolean = false,
    @SerialName("bestSeller") val isBestseller: Boolean,
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false
)

fun BookDto.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title,
        synopsis = this.synopsis ?: "Sin sinopsis disponible",
        author = "${this.authorObj.nombre} ${this.authorObj.apellidos}",
        publisher = this.publisherObj.nombre,
        genres = listOf(this.category),
        isBestseller = this.isBestseller,
        isFavorite = this.isFavorite,
        isBookmarked = this.isBookmarked,
        isToReturn = this.isToReturn,
        isLoaned = this.isLoaned // Pass the isLoaned value
    )
}

fun Book.toDto(): BookDto {
    val nameParts = this.author.split(" ", limit = 2)
    val nombre = nameParts.getOrNull(0) ?: ""
    val apellidos = nameParts.getOrNull(1) ?: ""

    return BookDto(
        id = this.id,
        title = this.title,
        synopsis = this.synopsis,
        authorObj = AuthorDto(nombre = nombre, apellidos = apellidos),
        publisherObj = EditorialDto(nombre = this.publisher),
        category = this.genres.firstOrNull() ?: "General",
        isBestseller = this.isBestseller,
        isFavorite = this.isFavorite,
        isBookmarked = this.isBookmarked,
        isToReturn = this.isToReturn,
        isLoaned = this.isLoaned
    )
}

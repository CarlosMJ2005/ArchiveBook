package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.domain.Book
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(
    @SerialName("nombre") val nombre: String,
    @SerialName("apellidos") val apellidos: String
)

@Serializable
data class EditorialDto(
    @SerialName("nombre") val nombre: String
)

// 2. Tu DTO principal adaptado EXACTAMENTE al JSON de la imagen
@Serializable
data class BookDto(
    @SerialName("idLibro") val id: Int,
    @SerialName("titulo") val title: String,

    // Puede venir null en el JSON, así que ponemos String? (nullable)
    @SerialName("sinopsis") val synopsis: String?,

    // Estos son OBJETOS, no Strings
    @SerialName("autor") val authorObj: AuthorDto,
    @SerialName("editorial") val publisherObj: EditorialDto,

    @SerialName("categoria") val category: String, // Es un String, no una lista en el JSON
    @SerialName("bestSeller") val isBestseller: Boolean,

    // Campos locales (no vienen en el JSON, se ponen por defecto)
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false
)

// 3. El Mapper: Aquí transformamos la estructura compleja del JSON a tu Domain simple
fun BookDto.toDomain(): Book {
    return Book(
        id = this.id,
        title = this.title,
        // Si la sinopsis es null, ponemos un texto por defecto
        synopsis = this.synopsis ?: "Sin sinopsis disponible",

        // Concatenamos nombre y apellidos para formar el String "author" que quiere tu Domain
        author = "${this.authorObj.nombre} ${this.authorObj.apellidos}",

        // Sacamos solo el nombre de la editorial
        publisher = this.publisherObj.nombre,

        // Convertimos la categoría única en una lista para tu Domain
        genres = listOf(this.category),

        isBestseller = this.isBestseller,
        isFavorite = this.isFavorite,
        isBookmarked = this.isBookmarked,
        isToReturn = this.isToReturn
    )
}
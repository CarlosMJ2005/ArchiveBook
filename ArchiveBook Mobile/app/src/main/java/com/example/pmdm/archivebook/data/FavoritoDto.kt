package com.example.pmdm.archivebook.data

import kotlinx.serialization.Serializable

@Serializable
data class FavoritoDto(
    val idFavorito: Int,
    val libro: BookDto // Aquí es donde realmente está el libro
)
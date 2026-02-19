package com.example.pmdm.archivebook.data

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkDto(
    val idPorLeer: Int, // Ajusta este nombre según lo que devuelva api/porLeer
    val libro: BookDto
)
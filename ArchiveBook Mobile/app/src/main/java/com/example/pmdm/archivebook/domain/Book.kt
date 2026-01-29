package com.example.pmdm.archivebook.domain

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val publisher: String,
    val genre: String,
    val description: String,
    val imageUrl: String? = null, // Para las portadas
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false
)
package com.example.pmdm.archivebook.domain

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val editorial: String,
    val imageUrl: String? = null, // Para cuando pongas portadas reales
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false
)
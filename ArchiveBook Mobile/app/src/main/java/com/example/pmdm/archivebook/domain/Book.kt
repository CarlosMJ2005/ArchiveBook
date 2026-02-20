package com.example.pmdm.archivebook.domain

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val synopsis: String,
    val author: String,
    val publisher: String,
    val genres: List<String>,
    val isBestseller: Boolean = false,
    val isFavorite: Boolean = false,
    val isBookmarked: Boolean = false,
    val isToReturn: Boolean = false,
    val isLoaned: Boolean = false
)
package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.navigation3.runtime.NavKey
import com.example.pmdm.archivebook.domain.Book
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreenKey : NavKey

@Serializable
data object RegisterScreenKey : NavKey

@Serializable
data object LibraryScreenKey : NavKey

@Serializable
data class BookDetailScreenKey(val book: Book) : NavKey

package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Definición de los destinos de navegación.
 * Se usa 'object' para rutas estáticas y 'data class' para rutas con argumentos.
 */

@Serializable
object LoginScreenKey : NavKey

@Serializable
object RegisterScreenKey : NavKey

@Serializable
object LibraryScreenKey : NavKey

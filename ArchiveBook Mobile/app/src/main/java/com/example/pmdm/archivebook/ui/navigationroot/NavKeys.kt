package com.example.pmdm.archivebook.ui.navigationroot

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Definición de los destinos de navegación.
 * Se usa 'object' para rutas estáticas y 'data class' para rutas con argumentos.
 */

@Serializable
object LoginKey : NavKey

@Serializable
object RegisterKey : NavKey

@Serializable
object LibraryKey : NavKey
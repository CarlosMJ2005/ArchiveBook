package com.example.pmdm.archivebook.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    @SerialName("correo")
    val email: String? = null
)

@Serializable
data class PrestamoDto(
    val libro: BookDto,
    val devuelto: Boolean,
    val usuario: UsuarioDto? = null
)

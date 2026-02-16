package com.example.pmdm.archivebook.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val contrasena: String,
    val rol: String = "USER" // Valor por defecto
)
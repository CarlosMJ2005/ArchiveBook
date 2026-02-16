package com.example.pmdm.archivebook.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("correo") val email: String,
    @SerialName("contrasena") val contrasena: String,
    @SerialName("role") val rol: String = "USER"
)
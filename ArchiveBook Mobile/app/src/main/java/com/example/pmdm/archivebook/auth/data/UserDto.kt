package com.example.pmdm.archivebook.auth.data

import com.example.pmdm.archivebook.auth.domain.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("correo") val email: String,
    @SerialName("contrasena") val contrasena: String,
    @SerialName("role") val rol: String = "USER"
)

fun User.toDto(): UserDto {
    return UserDto(
        email = this.email,
        contrasena = this.password,
        rol = "USER"
    )
}
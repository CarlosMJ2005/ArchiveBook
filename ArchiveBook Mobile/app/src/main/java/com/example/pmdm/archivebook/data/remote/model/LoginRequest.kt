package com.example.pmdm.archivebook.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    @SerialName("password")
    val password: String
)

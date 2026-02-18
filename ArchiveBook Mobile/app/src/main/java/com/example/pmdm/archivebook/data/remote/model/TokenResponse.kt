package com.example.pmdm.archivebook.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String
)
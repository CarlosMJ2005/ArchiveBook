package com.example.pmdm.archivebook.data.remote

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    @SerializedName("token") // Esto asegura que mapee bien el JSON {"token": "..."}
    val token: String
)
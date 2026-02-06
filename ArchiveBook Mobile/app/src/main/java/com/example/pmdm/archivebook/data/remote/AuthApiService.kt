package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthApiService(private val client: HttpClient) {
    suspend fun getToken(request: LoginRequest): String {
        val response = client.post("/token") {
            setBody(request)
            contentType(ContentType.Application.Json)
        }

        return response.bodyAsText()
    }
}

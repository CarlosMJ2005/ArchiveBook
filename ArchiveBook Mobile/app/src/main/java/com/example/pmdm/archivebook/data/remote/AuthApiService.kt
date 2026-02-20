package com.example.pmdm.archivebook.data.remote

import android.util.Log
import com.example.pmdm.archivebook.auth.data.UserDto
import com.example.pmdm.archivebook.auth.data.toDto
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import com.example.pmdm.archivebook.di.AuthFree
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class AuthApiService(private val client: HttpClient) {

    // Cambiamos la URL a una constante para no repetirla
    private val BASE_URL = "http://192.168.0.12:8080"
    //192.168.0.12
    //10.75.204.184
    suspend fun getToken(request: LoginRequest): String {
        val tempClient = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        try {
            val authString = "${request.email}:${request.password}"
            val encodedAuth = android.util.Base64.encodeToString(
                authString.toByteArray(),
                android.util.Base64.NO_WRAP
            )

            val response = tempClient.post("$BASE_URL/token") {
                headers.set(HttpHeaders.Authorization, "Basic $encodedAuth")
            }

            return when (response.status) {
                HttpStatusCode.OK -> response.bodyAsText()
                HttpStatusCode.Unauthorized, HttpStatusCode.Forbidden -> throw Exception("INVALID_CREDENTIALS")
                else -> throw Exception("SERVER_ERROR: ${response.status}")
            }
        } finally {
            tempClient.close()
        }
    }

    suspend fun register(user: User): Boolean {
        // 1. Usamos 'client' (la variable del constructor)
        // 2. IMPORTANTE: El registro suele ser una ruta pública, usamos el cliente normal
        val response = client.post("$BASE_URL/api/usuarios") {
            contentType(ContentType.Application.Json)
            setBody(user.toDto())
        }

        return when (response.status) {
            HttpStatusCode.Created, HttpStatusCode.OK -> true
            HttpStatusCode.Conflict -> throw Exception("User already exists")
            HttpStatusCode.BadRequest -> {
                val errorText = response.bodyAsText()
                if (errorText.contains("already exists", ignoreCase = true)) {
                    throw Exception("EMAIL_EXISTS")
                } else {
                    throw Exception("BAD_REQUEST")
                }
            }
            else -> throw Exception("UNKNOWN_ERROR: ${response.status}")
        }
    }
}
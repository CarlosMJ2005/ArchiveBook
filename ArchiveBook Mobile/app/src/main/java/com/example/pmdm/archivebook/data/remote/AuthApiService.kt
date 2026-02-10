package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthApiService(private val client: HttpClient) {

    suspend fun getToken(request: LoginRequest): String {
        val response = client.post("http://10.56.193.184:8080/token") {
            contentType(ContentType.Application.Json)
            basicAuth(username = request.email, password = request.password)
            setBody(request)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val tokenResponse = response.body<TokenResponse>()
                tokenResponse.token
            }
            HttpStatusCode.Unauthorized -> {
                throw Exception("401: No autorizado. Revisa credenciales o configuración del servidor.")
            }
            else -> {
                throw Exception("Error del servidor: ${response.status.value}")
            }
        }
    }
}

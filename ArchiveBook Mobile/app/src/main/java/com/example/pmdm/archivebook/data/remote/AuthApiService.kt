package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.auth.data.UserDto
import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class AuthApiService(private val client: HttpClient) {

    suspend fun getToken(request: LoginRequest): String {
        val response = client.post("token") {
            // The server expects Basic Auth, so we send the plain-text credentials
            // in the header and do NOT send a body.
            basicAuth(username = request.email, password = request.password)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                response.bodyAsText()
            }
            HttpStatusCode.Unauthorized -> throw Exception("401: Credenciales incorrectas.")
            else -> throw Exception("Error ${response.status.value}: ${response.bodyAsText()}")
        }
    }

    suspend fun registerUser(userDto: UserDto): Boolean {
        val response = client.post("api/usuarios") {
            contentType(ContentType.Application.Json)
            setBody(userDto)
        }
        return response.status == HttpStatusCode.Created || response.status == HttpStatusCode.OK
    }
}

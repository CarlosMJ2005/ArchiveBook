package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.auth.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.post

class AuthApiService(private val client: HttpClient) {
    suspend fun getToken(user: User): TokenResponse {
        return client.post("token") {
            // Ktor usará la configuración Basic Auth del HttpClient si la necesitas,
            // o puedes pasar las credenciales aquí manualmente
            basicAuth(user.email, user.password)
        }.body()
    }
}
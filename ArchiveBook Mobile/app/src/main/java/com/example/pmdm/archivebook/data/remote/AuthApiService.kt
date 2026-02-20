package com.example.pmdm.archivebook.data.remote

import android.util.Log
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
        val response = client.post("token") { // O "api/token" si corresponde
            // Inyecta el header Authorization: Basic ...
            basicAuth(username = request.email, password = request.password)

            // FORZAMOS el cuerpo a un String vacío.
            // Esto cambia Content-Length a 0 pero asegura que el motor de Ktor
            // procese la petición como un POST válido.
            setBody("")
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.bodyAsText()
            HttpStatusCode.Unauthorized -> throw Exception("401: Credenciales incorrectas.")
            else -> {
                val errorBody = response.bodyAsText()
                Log.e("API_ERROR", "Error ${response.status.value}: $errorBody")
                throw Exception("Error ${response.status.value}: $errorBody")
            }
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

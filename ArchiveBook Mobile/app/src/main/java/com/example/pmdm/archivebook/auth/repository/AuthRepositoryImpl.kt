package com.example.pmdm.archivebook.auth.repository

import com.example.pmdm.archivebook.auth.data.UserDto
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import java.lang.Exception
import kotlin.Result

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(user: User): Result<String> {
        return try {
            // Limpieza previa para asegurar una sesión limpia
            authManager.clearToken()

            val request = LoginRequest(user.email, user.password)
            val token = apiService.getToken(request)

            authManager.saveToken(token, user.email)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(user: User): Result<Boolean> {
        return try {
            // 1. Usamos el apiService en lugar de httpClient.post directamente
            // Debes asegurarte de que tu apiService tenga una función 'register'
            val success = apiService.register(user)

            if (success) {
                Result.success(true)
            } else {
                Result.failure(Exception("El registro no fue exitoso"))
            }
        } catch (e: Exception) {
            // 2. Aquí caerán los errores 409 (Conflict) o 400 (Bad Request)
            // Ktor suele lanzar excepciones si el status no es 2xx
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }
}
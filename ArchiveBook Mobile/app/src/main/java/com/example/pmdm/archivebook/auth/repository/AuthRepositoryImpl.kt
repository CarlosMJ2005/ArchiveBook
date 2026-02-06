package com.example.pmdm.archivebook.auth.repository

import android.util.Log
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import java.lang.Exception
import kotlin.Result // <-- ASEGÚRATE DE QUE SEA ESTE RESULT

class AuthRepositoryImpl(
    private val apiService: AuthApiService, // Koin pasará esto automáticamente
    private val authManager: AuthManager    // Koin pasará esto automáticamente
) : AuthRepository {

    override suspend fun login(user: User): Result<String> {
        return try {
            // Llamamos al servicio de Ktor (AuthApiService debe ser una CLASE ahora)
            val token = apiService.getToken(LoginRequest(user.email, user.password))

            Log.d("API_AUTH", "¡ÉXITO! Token guardado: $token")

            // Guardamos el token para futuras peticiones
            authManager.saveToken(token)

            Result.success(token)
        } catch (e: Exception) {
            Log.e("API_AUTH", "Error en el proceso de login: ${e.message}")
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }

    override fun hasActiveSession(): Boolean {
        // Comprobamos si hay un token guardado en el AuthManager
        return !authManager.getToken().isNullOrBlank()
    }
}
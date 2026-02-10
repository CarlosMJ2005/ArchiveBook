package com.example.pmdm.archivebook.data

import android.util.Log
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.model.LoginRequest

class AuthRepositoryImpl(
    private val apiService: AuthApiService, // Inyectamos el servicio
    private val authManager: AuthManager    // Inyectamos el manager para el token
) : AuthRepository {

    // Requisito de tu interfaz AuthRepository
    override var body: LoginRequest = LoginRequest("", "")

    override suspend fun login(user: User): Result<String> {
        return try {
            val credentials = LoginRequest(
                email = user.email,
                password = user.password
            )

            // Delegamos la llamada de red al API Service
            val token = apiService.getToken(credentials)

            // Si llegamos aquí, getToken no lanzó excepción (fue un 200 OK)
            authManager.saveToken(token)
            Log.d("API_AUTH", "¡ÉXITO! Token guardado: $token")

            Result.success(token)
        } catch (e: Exception) {
            // Aquí capturamos el 401 o cualquier error que lance el ApiService
            Log.e("API_AUTH", "Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }

    override fun hasActiveSession(): Boolean {
        return !authManager.getToken().isNullOrBlank()
    }
}
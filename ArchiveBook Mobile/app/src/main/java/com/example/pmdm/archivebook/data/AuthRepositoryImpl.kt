package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.model.LoginRequest

class AuthRepositoryImpl(
    private val apiService: AuthApiService,
    private val authManager: AuthManager
) : AuthRepository {

    override suspend fun login(user: User): Result<String> {
        return try {
            // En Ktor, si getToken falla, saltará directamente al catch(e)
            val token = apiService.getToken(LoginRequest(user.email, user.password))


            android.util.Log.d("API_AUTH", "¡ÉXITO! Token: $token")
            authManager.saveToken(token)

            Result.success(token)
        } catch (e: Exception) {
            // Aquí caerán los errores 401 (Unauthorized), 404, o fallos de red
            android.util.Log.e("API_AUTH", "Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }

    override fun hasActiveSession(): Boolean {
        return authManager.getToken() != null
    }
}
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
    private val authManager: AuthManager,    // Koin pasará esto automáticamente
) : AuthRepository {

    override var body: LoginRequest = LoginRequest("", "")

    override suspend fun login(user: User): Result<String> {
        return try {
            // Update the 'body' property if your interface expects it to hold the current request
            body = LoginRequest(user.email, user.password)

            // 2. Delegate the network call to your ApiService
            val token = apiService.getToken(body)

            Log.d("API_AUTH", "¡ÉXITO! Token guardado: $token")

            // 3. Save the token
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
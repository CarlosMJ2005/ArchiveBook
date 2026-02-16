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

    // LOGIN FUNCTION
    override suspend fun login(user: User): Result<String> {
        return try {
            val request = LoginRequest(user.email, user.password)
            val token = apiService.getToken(request)

            authManager.saveToken(token)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // REGISTER FUNCTION
    override suspend fun register(user: User): Result<Boolean> {
        return try {
            // Convertimos User (dominio) a UserDto (red)
            val userDto = UserDto(
                email = user.email,
                contrasena = user.password,
                rol = "USER" // Aquí inyectas el rol fijo
            )

            // Enviamos el DTO al servicio de API
            val success = apiService.registerUser(userDto)

            if (success) Result.success(true)
            else Result.failure(Exception("Error en el registro"))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }
}
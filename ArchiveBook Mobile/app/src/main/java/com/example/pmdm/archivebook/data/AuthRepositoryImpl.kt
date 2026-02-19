package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.auth.data.UserDto
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
            val token = apiService.getToken(LoginRequest(user.email, user.password))
            authManager.saveToken(token, user.email)
            Result.success(token)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(user: User): Result<Boolean> {
        return try {
            val userDto = UserDto(
                email = user.email,
                contrasena = user.password,
                rol = "USER"
            )

            val success = apiService.registerUser(userDto)
            Result.success(success)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }

}
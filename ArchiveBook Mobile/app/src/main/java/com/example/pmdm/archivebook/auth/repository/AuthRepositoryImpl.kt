package com.example.pmdm.archivebook.auth.repository

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
            val request = LoginRequest(user.email, user.password)
            val success = apiService.registerUser(request)

            if (success) {
                Result.success(true)
            } else {
                Result.failure(Exception("Could not create account"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        authManager.clearToken()
    }
}
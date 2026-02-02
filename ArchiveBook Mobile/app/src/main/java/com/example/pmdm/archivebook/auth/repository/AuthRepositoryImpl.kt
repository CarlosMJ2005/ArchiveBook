package com.example.pmdm.archivebook.auth.repository

import com.example.pmdm.archivebook.auth.domain.model.User //
import com.example.pmdm.archivebook.auth.data.remote.NetworkModule
import java.lang.Exception
import kotlin.Result // <-- ASEGÚRATE DE QUE SEA ESTE RESULT

class AuthRepositoryImpl : AuthRepository {

    private val api = NetworkModule.authApiService

    override suspend fun login(user: User): Result<String> {
        return try {
            val response = api.getToken(user)

            if (response.isSuccessful) {
                val token = response.body()?.token ?: ""
                Result.success(token)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
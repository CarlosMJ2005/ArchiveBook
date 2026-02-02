package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/token")
    suspend fun getToken(@Body user: User): Response<TokenResponse>
}

data class TokenResponse(
    val token: String
)

// Implementación del repositorio
class AuthRepositoryImpl(private val apiService: AuthApiService) : AuthRepository {
    override suspend fun login(user: User): Result<String> {
        return try {
            val response = apiService.getToken(user)
            if (response.isSuccessful) {
                Result.success(response.body()?.token ?: "")
            } else {
                Result.failure(Exception("Error en login: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
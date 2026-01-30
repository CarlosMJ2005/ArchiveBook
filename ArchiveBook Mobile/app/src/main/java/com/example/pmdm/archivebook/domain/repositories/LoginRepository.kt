package com.example.pmdm.archivebook.domain.repositories

interface LoginRepository {
    suspend fun login(email: String, pass: String): Result<Boolean>
    suspend fun register(email: String, pass: String): Result<Boolean>
    suspend fun saveSession(keepActive: Boolean)
}
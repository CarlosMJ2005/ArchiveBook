package com.example.pmdm.archivebook.domain.repositories

import com.example.pmdm.archivebook.auth.domain.model.User

interface LoginRepository {
    suspend fun login(user: User): Result<String>
    suspend fun register(user: User): Result<Boolean>
    suspend fun saveSession(keepActive: Boolean)
    suspend fun isSessionActive(): Boolean
    suspend fun logout()
}
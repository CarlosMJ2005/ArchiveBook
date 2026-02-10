package com.example.pmdm.archivebook.auth.repository

import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.data.remote.model.LoginRequest

interface AuthRepository {
    var body: LoginRequest

    suspend fun login(user: User): Result<String>
    fun logout()
    fun hasActiveSession(): Boolean
}
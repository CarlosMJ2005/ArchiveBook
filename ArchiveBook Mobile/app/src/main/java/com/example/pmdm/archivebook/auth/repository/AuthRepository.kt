package com.example.pmdm.archivebook.auth.repository

import com.example.pmdm.archivebook.auth.domain.model.User

interface AuthRepository {
    suspend fun login(user: User): Result<String>
}
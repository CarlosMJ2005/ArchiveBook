package com.example.pmdm.archivebook.auth.repository

import com.example.pmdm.archivebook.auth.domain.model.User

/**
 * A repository for handling core authentication logic.
 */
interface AuthRepository {
    suspend fun login(user: User): Result<String>
    suspend fun register(user: User): Result<Boolean>
    fun logout()
}
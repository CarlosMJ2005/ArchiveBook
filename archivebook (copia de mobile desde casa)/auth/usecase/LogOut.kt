package com.example.pmdm.archivebook.auth.usecase

import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.di.HttpClientProvider
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository

class LogOut(
    private val authRepository: AuthRepository,
    private val libraryRepository: LibraryRepository,
    private val httpClientProvider: HttpClientProvider
) {
    operator fun invoke() {
        // 1. Clear the authentication token from storage
        authRepository.logout()
        // 2. Clear the in-memory data cache
        libraryRepository.clearCache()
        // 3. Destroy the HTTP client to clear its internal token cache
        httpClientProvider.reset()
    }
}
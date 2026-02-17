package com.example.pmdm.archivebook.auth.usecase

import com.example.pmdm.archivebook.auth.repository.AuthRepository

class LogOut(private val repository: AuthRepository) {
    operator fun invoke() {
        repository.logout()
    }
}
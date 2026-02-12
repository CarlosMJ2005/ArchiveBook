package com.example.pmdm.archivebook.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.domain.repositories.LoginRepository
import com.example.pmdm.archivebook.presentation.LoginViewModel

class LoginViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository as AuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
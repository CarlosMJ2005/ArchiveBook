package com.example.pmdm.archivebook.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.domain.repositories.LoginRepository
import com.example.pmdm.archivebook.presentation.RegisterViewModel

class RegisterViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(repository as AuthRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
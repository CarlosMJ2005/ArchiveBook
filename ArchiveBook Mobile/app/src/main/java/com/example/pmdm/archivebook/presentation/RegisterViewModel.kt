package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.repositories.LoginRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: LoginRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var keepSession by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onRegisterClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val result = repository.register(email, password)
            if (result.isSuccess) {
                repository.saveSession(keepSession)
                onSuccess()
            } else {
                onError("Registration failed")
            }
            isLoading = false
        }
    }
}
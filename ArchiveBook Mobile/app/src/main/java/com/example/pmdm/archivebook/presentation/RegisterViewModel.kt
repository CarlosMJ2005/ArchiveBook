package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var keepSession by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onRegisterClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val user = User(email = email, password = password)
            val result = repository.register(user)
            if (result.isSuccess) {
                // After a successful registration, we log in to get the token
                val loginResult = repository.login(user)
                if (loginResult.isSuccess) {
                    onSuccess()
                } else {
                    onError("Registration succeeded but login failed")
                }
            } else {
                onError("Registration failed")
            }
            isLoading = false
        }
    }
}

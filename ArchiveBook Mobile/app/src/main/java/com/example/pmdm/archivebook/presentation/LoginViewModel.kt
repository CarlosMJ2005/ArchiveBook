package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.domain.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var keepSession by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onLoginClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = repository.login(email, password)

                // --- AÑADE ESTA LÍNEA ---
                android.util.Log.d("DEBUG_TOKEN", "Respuesta del servidor: $result")

                if (result != null) {
                    // Si usas tokens JWT, suelen empezar por "eyJ..."
                    onSuccess()
                } else {
                    onError("Invalid credentials")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_TOKEN", "Error en login: ${e.message}")
                onError(e.message ?: "Unknown error")
            } finally {
                isLoading = false
            }
        }
    }
}
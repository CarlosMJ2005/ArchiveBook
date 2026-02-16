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
        // 1. Validación local básica
        if (email.isBlank() || password.isBlank()) {
            onError("Por favor, rellena todos los campos")
            return
        }

        if (password != confirmPassword) {
            onError("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            isLoading = true
            val user = User(email = email, password = password)

            // El repositorio ahora se encarga de crear el UserDto con el rol 'USER'
            val result = repository.register(user)

            result.fold(
                onSuccess = {
                    // Registro OK, ahora intentamos login para obtener el token
                    val loginResult = repository.login(user)
                    loginResult.fold(
                        onSuccess = { onSuccess() },
                        onFailure = { onError("Cuenta creada, pero hubo un error al iniciar sesión automáticamente") }
                    )
                },
                onFailure = { error ->
                    // Aquí verás el error real en el Logcat
                    onError("Error en el registro: ${error.message ?: "Error desconocido"}")
                }
            )
            isLoading = false
        }
    }
}

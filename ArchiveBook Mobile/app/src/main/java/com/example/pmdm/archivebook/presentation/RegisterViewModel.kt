package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.data.local.AuthManager
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository,
    private val authManager: AuthManager
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var keepSession by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onRegisterClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
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

            val result = repository.register(user)

            result.fold(
                onSuccess = {
                    // 2. Registro OK, intentamos login automático
                    val loginResult = repository.login(user)
                    loginResult.fold(
                        onSuccess = { token ->
                            // 3. ¡ESTA ES LA CLAVE!
                            // Guardamos la preferencia del switch antes de navegar
                            authManager.saveKeepSession(keepSession)

                            // El token se guarda dentro del login del repositorio,
                            // pero nos aseguramos de que el switch se guarde aquí.
                            onSuccess()
                        },
                        onFailure = {
                            onError("Cuenta creada, pero hubo un error al iniciar sesión automáticamente")
                        }
                    )
                },
                onFailure = { error ->
                    // error.message suele traer lo que el servidor responde (ej: "Email already in use")
                    val friendlyMessage = when {
                        error.message?.contains("409") == true -> "This email is already registered."
                        error.message?.contains("400") == true -> "Invalid data. Please check your email format."
                        else -> "Registration failed: ${error.message ?: "Unknown error"}"
                    }
                    onError(friendlyMessage)
                }
            )
            isLoading = false
        }
    }
}
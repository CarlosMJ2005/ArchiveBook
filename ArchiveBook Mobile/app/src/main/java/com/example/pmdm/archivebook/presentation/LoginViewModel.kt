package com.example.pmdm.archivebook.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pmdm.archivebook.auth.domain.model.User
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.di.HttpClientProvider
import com.example.pmdm.archivebook.domain.errors.InvalidCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val repository: AuthRepository,
    private val httpClientProvider: HttpClientProvider,
    private val authManager: AuthManager // <--- Añadido
) : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var keepSession by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onLoginClicked(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onError("Fill all the fields, please.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { isLoading = true }

                // 1. Limpieza de seguridad: storage y memoria RAM del cliente HTTP
                repository.logout()
                httpClientProvider.reset()

                // 2. Intento de login
                val result = repository.login(User(email = email, password = password))

                withContext(Dispatchers.Main) {
                    result.onSuccess { token ->
                        if (token.isNotBlank()) {
                            // 1. Guardamos el estado del Switch en el AuthManager
                            // Usamos la variable 'keepSession' que está vinculada al Switch de la UI
                            authManager.saveKeepSession(keepSession)

                            // 2. El token ya debería guardarse dentro de tu repositorio o aquí mismo
                            // authManager.saveToken(token, email)

                            onSuccess()
                        } else {
                            onError("Error: Server didn't return a valid token.")
                        }
                    }.onFailure { e ->
                        // 3. EDITAR MENSAJE DE ERROR AQUÍ
                        val friendlyMessage = when (e.message) {
                            "INVALID_CREDENTIALS" -> "Invalid credentials. Try again."
                            else -> "Error in the server"
                        }
                        onError(friendlyMessage)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.util.Log.e("LOGIN_ERROR", "Error de red: ${e.message}")
                    onError("Unable to connect to the server. Please check your connection.")
                }
            } finally {
                withContext(Dispatchers.Main) { isLoading = false }
            }
        }
    }

    fun clearFields() {
        email = ""
        password = ""
    }
}
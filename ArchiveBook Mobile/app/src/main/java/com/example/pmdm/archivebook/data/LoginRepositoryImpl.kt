package com.example.pmdm.archivebook.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore // This was the missing one
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.model.LoginRequest
import com.example.pmdm.archivebook.domain.repositories.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore by preferencesDataStore(name = "settings")

class LoginRepositoryImpl(
    private val context: Context,
    private val authApiService: AuthApiService // Inyectado por Koin
) : LoginRepository {

    private val KEEP_SESSION_KEY = booleanPreferencesKey("keep_session")

    override suspend fun login(email: String, pass: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            // Creamos un objeto LoginRequest para la petición
            val loginRequest = LoginRequest(
                username = email,
                passw = pass
            )

            // Llamada a Ktor
            val token = authApiService.getToken(loginRequest)

            Log.d("API_AUTH", "¡TOKEN RECIBIDO!: $token")

            Result.success(token)
        } catch (e: Exception) {
            Log.e("API_AUTH", "Error en login: ${e.message}")
            // Ktor lanza excepciones para 401, 500, etc., si no se capturan de otra forma
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val loginRequest = LoginRequest(
                username = email,
                passw = pass
            )
            // Aquí iría tu lógica de registro real cuando la tengas
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveSession(keepActive: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[KEEP_SESSION_KEY] = keepActive
            }
        }
    }

    override suspend fun isSessionActive(): Boolean = withContext(Dispatchers.IO) {
        return@withContext context.dataStore.data
            .map { preferences -> preferences[KEEP_SESSION_KEY] ?: false }
            .first()
    }
}
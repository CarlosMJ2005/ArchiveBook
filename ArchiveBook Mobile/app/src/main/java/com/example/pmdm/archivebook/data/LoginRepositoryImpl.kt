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

// Note: Keep this outside the class so it's a singleton extension
private val Context.dataStore by preferencesDataStore(name = "settings")

class LoginRepositoryImpl(
    private val context: Context,
    private val authApiService: AuthApiService
) : LoginRepository {

    private val keepSessionKey = booleanPreferencesKey("keep_session")

    override suspend fun login(email: String, pass: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val loginRequest = LoginRequest(email = email, password = pass)
            val token = authApiService.getToken(loginRequest)
            Log.d("API_AUTH", "¡TOKEN RECIBIDO!: $token")
            Result.success(token)
        } catch (e: Exception) {
            Log.e("API_AUTH", "Error en login: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String): Result<Boolean> = withContext(Dispatchers.IO) {
        return@withContext try {
            val loginRequest = LoginRequest(
                email = email,
                password = pass
            )

            Log.d("API_AUTH", "Intentando registrar usuario: ${loginRequest.email}")

            Result.success(true)
        } catch (e: Exception) {
            Log.e("API_AUTH", "Error en registro: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun saveSession(keepActive: Boolean) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { settings ->
                settings[keepSessionKey] = keepActive
            }
        }
    }

    override suspend fun isSessionActive(): Boolean = withContext(Dispatchers.IO) {
        return@withContext context.dataStore.data
            .map { preferences -> preferences[keepSessionKey] ?: false }
            .first()
    }
}
package com.example.pmdm.archivebook.di

import android.util.Log
import com.example.pmdm.archivebook.data.local.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json


val AuthFree = AttributeKey<Boolean>("AuthFree")

class HttpClientProvider(private val authManager: AuthManager) {

    private var client: HttpClient? = null
    private val lock = Any()

    private fun create(): HttpClient {
        return HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("HTTP_KTOR", message)
                    }
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val token = authManager.getToken()
                        if (!token.isNullOrBlank()) {
                            BearerTokens(token, "")
                        } else {
                            null
                        }
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        // Si la URL contiene "token", NO enviar nunca el Bearer token automáticamente
                        path.contains("token") || request.attributes.getOrNull(AuthFree) ?: false
                    }
                }
            }

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "192.168.0.12"
                    port = 8080
                }
            }
        }
    }

    fun get(): HttpClient {
        return synchronized(lock) {
            client ?: create().also { client = it }
        }
    }

    // Asegúrate de que esta función esté DENTRO de la clase y sea pública
    fun reset() {
        synchronized(lock) {
            Log.d("HTTP_KTOR", "Reseteando cliente...")
            client = null
        }
    }
}
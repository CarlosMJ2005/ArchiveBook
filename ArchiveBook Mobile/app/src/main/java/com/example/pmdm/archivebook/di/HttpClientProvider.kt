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
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class HttpClientProvider(private val authManager: AuthManager) {

    private var client: HttpClient? = null

    fun create(): HttpClient {
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
                    // Solo devuelve TRUE para las rutas que NO requieren token
                    sendWithoutRequest { request ->
                        val path = request.url.buildString()
                        val isLogin = path.endsWith("/api/login")
                        val isRegister = path.endsWith("/api/usuarios") && request.method == HttpMethod.Post

                        isLogin || isRegister
                    }
                }
            }
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTP
                    host = "10.75.204.184"
                    port = 8080
                }
            }
        }
    }

    fun get(): HttpClient {
        if (client == null) {
            client = create()
        }
        return client!!
    }

    fun reset() {
        client = null
        client = create()
    }
}
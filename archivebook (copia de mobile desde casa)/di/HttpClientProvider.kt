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
import kotlin.text.isNullOrBlank

class HttpClientProvider(private val authManager: AuthManager) {

    private var client: HttpClient? = null

    // Make create public to allow for re-creation of the client
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
                            BearerTokens(token, "") // The second parameter is a refresh token, which we're not using.
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        val urlString = request.url.buildString()
                        // Do not send the Bearer token for login or user creation requests.
                        val isTokenRequest = urlString.endsWith("/token")
                        val isUserCreationRequest = urlString.contains("/usuarios") && request.method == HttpMethod.Post
                        isTokenRequest || isUserCreationRequest
                    }
                }
            }
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTP
                    host = "192.168.0.12"
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

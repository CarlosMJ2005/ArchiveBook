package com.example.pmdm.archivebook.di

import android.util.Log
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.data.AuthRepositoryImpl
import com.example.pmdm.archivebook.data.LibraryRepositoryImpl
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.data.remote.LibraryApiService
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import com.example.pmdm.archivebook.presentation.LoginViewModel
import com.example.pmdm.archivebook.presentation.RegisterViewModel
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
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // 1. Persistencia local
    single { AuthManager(androidContext()) }

    // 2. Cliente de Red (Ktor)
    single {
        // We need to get AuthManager here for the Auth plugin
        val authManager: AuthManager = get()
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    }
                )
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
                        Log.d("AUTH_DEBUG", "Cargando token para la petición: $token")
                        if (token != null) BearerTokens(token, "") else null
                    }
                    sendWithoutRequest {
                        // We don't want to send the token when we are asking for a token
                        !it.url.pathSegments.contains("token") || !it.url.pathSegments.contains("api/usuarios")
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

    // 3. Servicios de API
    single { AuthApiService(get()) }
    single { LibraryApiService(client = get(), authManager = get()) }


    // 4. Repositorios
    // Restore authManager here
    single<AuthRepository> { AuthRepositoryImpl(apiService = get(), authManager = get()) }
    single<LibraryRepository> { LibraryRepositoryImpl(get()) }

    // 5. ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { (id: Int) -> BookDetailViewModel(id, get()) }
}
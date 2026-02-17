package com.example.pmdm.archivebook.di

import android.util.Log
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.auth.usecase.LogOut
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
                        if (token != null) {
                            BearerTokens(token, "") // El refresh token no se usa aquí
                        } else {
                            null
                        }
                    }
                    // NO enviar el token en estas rutas
                    sendWithoutRequest { request ->
                        val urlString = request.url.toString()
                        urlString.endsWith("/token") || urlString.endsWith("/api/usuarios")
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
    single { LibraryApiService(get(), get()) }

    // 4. Repositorios
    single<AuthRepository> { AuthRepositoryImpl(apiService = get(), authManager = get()) }
    single<LibraryRepository> { LibraryRepositoryImpl(get()) }

    // 5. Casos de Uso
    single { LogOut(repository = get()) }

    // 6. ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LibraryViewModel(get()) }
    viewModel { (id: Int) -> BookDetailViewModel(id, get()) }
}
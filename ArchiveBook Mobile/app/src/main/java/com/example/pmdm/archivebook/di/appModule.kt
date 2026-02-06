package com.example.pmdm.archivebook.di

import android.util.Log
import com.example.pmdm.archivebook.auth.repository.AuthRepository
import com.example.pmdm.archivebook.auth.repository.AuthRepositoryImpl
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.data.LibraryRepositoryImpl
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.presentation.LibraryViewModel
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // 1. Persistencia local
    single { AuthManager(androidContext()) }

    // 2. Cliente de Red (Ktor)
    single {
        val authManager: AuthManager = get()
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
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
                            BearerTokens(token, "") // El refresh token no lo usamos ahora
                        } else {
                            null
                        }
                    }
                    sendWithoutRequest {
                        it.url.pathSegments.contains("token")
                    }
                }
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                    host = "10.56.193.184"
                    port = 8080
                }
            }
        }
    }

    // 3. Servicios de API
    single<AuthApiService> {
        AuthApiService(
            get()
        ) }


    single<AuthRepository> {
        AuthRepositoryImpl(
            apiService = get(),
            authManager = get()
        )
    }

    single<LibraryRepository> { LibraryRepositoryImpl(
        client = get()
    ) }

    // LibraryViewModel doesn't need parameters
    viewModel { LibraryViewModel(get()) }

    // --- NEW: BookDetailViewModel with parameters ---
    // The (id: Int) matches the parametersOf(id) call in your Screen
    viewModel { (id: Int) ->
        BookDetailViewModel(
            repository = get(),
            bookId = id
        )
    }

    // 5. ViewModels
    viewModelOf(::LibraryViewModel)
}
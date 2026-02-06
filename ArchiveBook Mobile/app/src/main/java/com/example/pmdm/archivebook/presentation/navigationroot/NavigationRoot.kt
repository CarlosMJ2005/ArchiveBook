package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.data.LibraryRepositoryImpl
import com.example.pmdm.archivebook.data.LoginRepositoryImpl
import com.example.pmdm.archivebook.data.remote.AuthApiService
import com.example.pmdm.archivebook.di.BookDetailViewModelFactory
import com.example.pmdm.archivebook.di.LibraryViewModelFactory
import com.example.pmdm.archivebook.di.LoginViewModelFactory
import com.example.pmdm.archivebook.di.RegisterViewModelFactory
import com.example.pmdm.archivebook.presentation.screens.BookDetailScreen
import com.example.pmdm.archivebook.presentation.screens.LibraryScreen
import com.example.pmdm.archivebook.presentation.screens.LoginScreen
import com.example.pmdm.archivebook.presentation.screens.RegisterScreen
import io.ktor.client.HttpClient
import org.koin.compose.koinInject


@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Inject the HttpClient configured in your Koin module
    val client: HttpClient = koinInject()
    val authApiService: AuthApiService = koinInject()

    val loginRepository = remember {
        LoginRepositoryImpl(
            context = context,
            authApiService = authApiService
        )
    }
    val libraryRepository = remember { LibraryRepositoryImpl(client = client) }

    // 3. Definimos las factories
    val loginFactory = remember { LoginViewModelFactory(loginRepository) }
    val registerFactory = remember { RegisterViewModelFactory(loginRepository) }

    val backStack = rememberNavBackStack(LoginScreenKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {

            entry<LoginScreenKey> {
                LoginScreen(
                    factory = loginFactory, // Ahora 'loginFactory' es visible
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(LibraryScreenKey)
                    },
                    onRegisterClick = {
                        backStack.add(RegisterScreenKey)
                    }
                )
            }

            entry<RegisterScreenKey> {
                RegisterScreen(
                    // USAMOS 'registerFactory' que definimos arriba
                    factory = registerFactory,
                    onRegisterSuccess = {
                        backStack.clear()
                        backStack.add(LibraryScreenKey)
                    },
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(LoginScreenKey)
                    }
                )
            }

            entry<LibraryScreenKey> {
                LibraryScreen(
                    factory = LibraryViewModelFactory(libraryRepository), // Now correctly initialized
                    onLogout = {
                        backStack.clear()
                        backStack.add(LoginScreenKey)
                    },
                    onBookClick = { id ->
                        backStack.add(BookDetailScreenKey(bookId = id))
                    }
                )
            }

            entry<BookDetailScreenKey> { entry ->
                val id = entry.bookId

                val detailFactory = remember(id) {
                    BookDetailViewModelFactory(libraryRepository, id)
                }

                BookDetailScreen(
                    factory = detailFactory,
                    onBack = { backStack.removeLastOrNull() }
                    // Ya no necesitas pasar 'book = ...' aquí
                )
            }
        }
    )
}
package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.auth.usecase.LogOut
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.presentation.screens.BookDetailScreen
import com.example.pmdm.archivebook.presentation.screens.LibraryScreen
import com.example.pmdm.archivebook.presentation.screens.LoginScreen
import com.example.pmdm.archivebook.presentation.screens.RegisterScreen
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import com.example.pmdm.archivebook.presentation.LoginViewModel
import com.example.pmdm.archivebook.presentation.RegisterViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    // 1. Inyectamos el AuthManager al principio para decidir dónde empezar
    val authManager: AuthManager = koinInject()

    // 2. Lógica de Auto-Login: si hay token y marcó "keepSession", vamos a la librería
    val startDestination = if (authManager.shouldKeepSession() && !authManager.getToken().isNullOrBlank()) {
        LibraryScreenKey
    } else {
        LoginScreenKey
    }

    // Usamos el startDestination dinámico
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(startDestination)

    if (backStack.isNotEmpty()) {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            entryProvider = entryProvider {

                entry<LoginScreenKey> {
                    val viewModel: LoginViewModel = koinViewModel()
                    val libraryViewModel: LibraryViewModel = koinViewModel()
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = {
                            libraryViewModel.fetchAllBooks()
                            backStack.clear()
                            backStack.add(LibraryScreenKey)
                        },
                        onRegisterClick = { backStack.add(RegisterScreenKey) }
                    )
                }

                entry<RegisterScreenKey> {
                    val viewModel: RegisterViewModel = koinViewModel()
                    val libraryViewModel: LibraryViewModel = koinViewModel()
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = {
                            libraryViewModel.fetchAllBooks()
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
                    val libraryViewModel: LibraryViewModel = koinViewModel()
                    val loginViewModel: LoginViewModel = koinViewModel()
                    val logOut: LogOut = koinInject()

                    LibraryScreen(
                        viewModel = libraryViewModel,
                        onLogout = {
                            // IMPORTANTE: Limpiar el AuthManager al cerrar sesión
                            authManager.clearToken()
                            logOut()
                            libraryViewModel.clearFilters()
                            loginViewModel.clearFields()
                            backStack.clear()
                            backStack.add(LoginScreenKey)
                        },
                        onBookClick = { book -> backStack.add(BookDetailScreenKey(book = book)) }
                    )
                }

                entry<BookDetailScreenKey> { entry ->
                    val book = entry.book
                    // Inyectamos el manager para obtener el token necesario para las imágenes
                    val currentAuthManager: AuthManager = koinInject()
                    val sessionToken = currentAuthManager.getToken() ?: ""

                    val viewModel: BookDetailViewModel = koinViewModel(
                        key = "book_detail_${book.id}",
                        parameters = {
                            parametersOf(book.id, sessionToken)
                        }
                    )

                    BookDetailScreen(
                        viewModel = viewModel,
                        onBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}
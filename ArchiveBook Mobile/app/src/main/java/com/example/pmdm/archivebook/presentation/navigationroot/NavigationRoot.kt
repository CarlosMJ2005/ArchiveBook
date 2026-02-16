package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.presentation.screens.BookDetailScreen
import com.example.pmdm.archivebook.presentation.screens.LibraryScreen
import com.example.pmdm.archivebook.presentation.screens.LoginScreen
import com.example.pmdm.archivebook.presentation.screens.RegisterScreen
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import com.example.pmdm.archivebook.presentation.LoginViewModel
import com.example.pmdm.archivebook.presentation.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginScreenKey)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryProvider = entryProvider {

            entry<LoginScreenKey> {
                val viewModel: LoginViewModel = koinViewModel()
                LoginScreen(
                    viewModel = viewModel, // Pasamos el VM de Koin
                    onLoginSuccess = {
                        backStack.clear()
                        backStack.add(LibraryScreenKey)
                    },
                    onRegisterClick = { backStack.add(RegisterScreenKey) }
                )
            }

            entry<RegisterScreenKey> {
                val viewModel: RegisterViewModel = koinViewModel()
                RegisterScreen(
                    viewModel = viewModel, // Pasamos el VM de Koin
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
                val libraryViewModel: LibraryViewModel = koinViewModel()
                val loginViewModel: LoginViewModel = koinViewModel()
                LibraryScreen(
                    viewModel = libraryViewModel,
                    onLogout = {
                        libraryViewModel.clearFields()
                        loginViewModel.clearFields()
                        backStack.clear()
                        backStack.add(LoginScreenKey)
                    },
                    onBookClick = { id -> backStack.add(BookDetailScreenKey(bookId = id)) }
                )
            }

            entry<BookDetailScreenKey> { entry ->
                val id = entry.bookId
                val viewModel: BookDetailViewModel = koinViewModel(
                    parameters = { parametersOf(id) }
                )
                BookDetailScreen(
                    viewModel = viewModel,
                    onBack = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
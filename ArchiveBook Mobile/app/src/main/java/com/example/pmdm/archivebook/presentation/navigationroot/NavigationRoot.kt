package com.example.pmdm.archivebook.presentation.navigationroot

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.pmdm.archivebook.auth.usecase.LogOut
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
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(LoginScreenKey)

    if (backStack.isNotEmpty()) {
        NavDisplay(
            backStack = backStack,
            modifier = modifier,
            entryProvider = entryProvider {

                entry<LoginScreenKey> {
                    val viewModel: LoginViewModel = koinViewModel()
                    val libraryViewModel: LibraryViewModel = koinViewModel()
                    LoginScreen(
                        viewModel = viewModel, // Pasamos el VM de Koin
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
                    val viewModel: BookDetailViewModel = koinViewModel(
                        key = "book_detail_${book.id}", // Clave única para cada ViewModel
                        parameters = { parametersOf(book.id) }
                    )
                    BookDetailScreen(
                        viewModel = viewModel,
                        onBack = fun() {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}

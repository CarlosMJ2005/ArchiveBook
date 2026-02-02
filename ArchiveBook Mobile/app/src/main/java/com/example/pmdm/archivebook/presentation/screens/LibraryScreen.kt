package com.example.pmdm.archivebook.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    // Inyectamos el ViewModel (puedes pasarlo desde el NavRoot o dejarlo por defecto)
    viewModel: LibraryViewModel = koinViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var showMenu by remember { mutableStateOf(false) }
    var showGenreMenu by remember { mutableStateOf(false) }

    val contentColor = if (isDarkTheme) Color(0xFFF5E6CC) else Color(0xFF7B241C)
    val selectedBg = if (isDarkTheme) Color(0xFF390A02) else Color(0xFFF8F2E4)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.background) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "ArchiveBook",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = contentColor.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(8.dp))

                val drawerItemColors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = selectedBg,
                    unselectedContainerColor = Color.Transparent,
                    selectedIconColor = contentColor,
                    unselectedIconColor = contentColor,
                    selectedTextColor = contentColor,
                    unselectedTextColor = contentColor
                )

                // --- SECCIONES PRINCIPALES ---

                NavigationDrawerItem(
                    label = { Text("Library") },
                    selected = true, // Aquí podrías usar una variable de estado para saber cuál está seleccionado
                    icon = { Icon(Icons.Default.Menu, null) },
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    colors = drawerItemColors,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Best Sellers") },
                    selected = false,
                    icon = { Icon(Icons.Default.Star, null) },
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    colors = drawerItemColors,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Favorites") },
                    selected = false,
                    icon = { Icon(Icons.Default.Favorite, null) },
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    colors = drawerItemColors,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Yet to read") },
                    selected = false,
                    icon = { Icon(Icons.Default.Bookmark, null) },
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    colors = drawerItemColors,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("To return") },
                    selected = false,
                    icon = { Icon(Icons.Default.NotificationsActive, null) },
                    onClick = { coroutineScope.launch { drawerState.close() } },
                    colors = drawerItemColors,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = contentColor.copy(alpha = 0.2f)
                )

                NavigationDrawerItem(
                    label = { Text("Log Out") },
                    selected = false,
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                    onClick = { coroutineScope.launch { onLogout() } },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    colors = drawerItemColors
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Surface(color = MaterialTheme.colorScheme.background, shadowElevation = 2.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Abrir menú", tint = contentColor)
                        }

                        TextField(
                            // CAMBIO: Si no hay géneros, mostramos el texto de ayuda como VALOR real
                            value = if (viewModel.selectedFilter == "Genre") {
                                if (viewModel.selectedGenres.isEmpty()) "Select genres in menu..."
                                else viewModel.selectedGenres.joinToString(", ")
                            } else {
                                viewModel.searchText
                            },
                            onValueChange = {
                                if (viewModel.selectedFilter != "Genre") viewModel.searchText = it
                            },
                            readOnly = viewModel.selectedFilter == "Genre",
                            modifier = Modifier.weight(1f).height(54.dp).padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = if (viewModel.selectedFilter == "Genre") Icons.Default.Mode else Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            },
                            trailingIcon = {
                                if (viewModel.searchText.isNotEmpty() || viewModel.selectedGenres.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.clearFilters() }) {
                                        Icon(
                                            Icons.Default.Close,
                                            null,
                                            modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.background
                                        )
                                    }
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.onSurface,
                                // CAMBIO: Si es el texto de ayuda de géneros, lo ponemos un poco más transparente
                                focusedTextColor = if (viewModel.selectedFilter == "Genre" && viewModel.selectedGenres.isEmpty()) {
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                                } else {
                                    MaterialTheme.colorScheme.background
                                },
                                unfocusedTextColor = if (viewModel.selectedFilter == "Genre" && viewModel.selectedGenres.isEmpty()) {
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.6f)
                                } else {
                                    MaterialTheme.colorScheme.background
                                },
                                cursorColor = MaterialTheme.colorScheme.background,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, "filters", tint = contentColor)
                            }

                            // MENÚ PRINCIPAL
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = {
                                    showMenu = false
                                    showGenreMenu = false
                                },
                                offset = DpOffset(x = (0).dp, y = 12.dp),
                                modifier = Modifier.background(contentColor)
                            ) {
                                val filters = listOf(
                                    "Title" to Icons.Default.Book,
                                    "Author" to Icons.Default.Person,
                                    "Publisher" to Icons.Default.Apartment,
                                    "Genre" to Icons.Default.Mode
                                )

                                filters.forEach { (name, icon) ->
                                    // Usamos el estado del ViewModel para saber qué está seleccionado
                                    val isSelected = viewModel.selectedFilter == name

                                    Surface(
                                        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = name,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                if (name == "Genre") {
                                                    viewModel.selectedFilter = "Genre" // <--- AÑADE ESTO: Activa el modo género
                                                    showGenreMenu = true
                                                } else {
                                                    viewModel.selectedFilter = name
                                                    viewModel.selectedGenres = emptySet()
                                                    viewModel.searchText = ""
                                                    showMenu = false
                                                }
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    icon,
                                                    null,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            },
                                            colors = MenuDefaults.itemColors(
                                                textColor = if (isSelected) contentColor else MaterialTheme.colorScheme.surface,
                                                leadingIconColor = if (isSelected) contentColor else MaterialTheme.colorScheme.surface
                                            )
                                        )
                                    }
                                }
                            }

                            // SUBMENÚ DE GÉNEROS
                            DropdownMenu(
                                expanded = showGenreMenu,
                                offset = DpOffset(x = (0).dp, y = 12.dp),
                                onDismissRequest = {
                                    showGenreMenu = false
                                    showMenu = false
                                },
                                modifier = Modifier.background(contentColor)
                            ) {
                                val genres =
                                    listOf("Fantasy", "Terror", "Sci-Fi", "Romance", "History")
                                genres.forEach { genre ->
                                    // Usamos el estado del ViewModel
                                    val isChecked = viewModel.selectedGenres.contains(genre)

                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                genre,
                                                color = MaterialTheme.colorScheme.surface
                                            )
                                        },
                                        onClick = {
                                            viewModel.toggleGenre(genre) // <--- Toda la lógica que tenías se resume aquí

                                            // Si quieres que se cierre todo al desmarcar el último género:
                                            if (viewModel.selectedGenres.isEmpty()) {
                                                showGenreMenu = false
                                                showMenu = false
                                            }
                                        },
                                        leadingIcon = {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = null,
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = MaterialTheme.colorScheme.surface,
                                                    uncheckedColor = MaterialTheme.colorScheme.surface,
                                                    checkmarkColor = contentColor
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->

            val listState = rememberLazyListState()

            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Obtenemos la lista ya filtrada del ViewModel
                val booksToShow = viewModel.filteredBooks

                when {
                    // Caso 1: Cargando datos
                    viewModel.isLoading -> {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = contentColor
                        )
                    }

                    // Caso 2: Error en la API
                    viewModel.errorMessage != null -> {
                        Text(
                            text = viewModel.errorMessage!!,
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Caso 3: Lista vacía (o sin resultados de búsqueda)
                    booksToShow.isEmpty() -> {
                        Text(
                            text = "There are no books available.",
                            modifier = Modifier.align(Alignment.Center),
                            color = contentColor
                        )
                    }

                    // Caso 4: Mostrar lista
                    else -> {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(booksToShow, key = { it.id }) { book ->
                                BookCard(
                                    book = book,
                                    onFavoriteClick = { viewModel.toggleFavorite(book.id) },
                                    onBookmarkClick = { viewModel.toggleBookmark(book.id) },
                                    onReturnClick = { viewModel.toggleReturn(book.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookCard(
    book: Book,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onReturnClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val titleColor = if (isDark) Color(0xFFF5E6CC) else Color(0xFF84240C)
    val authorColor = if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp), // Padding mínimo para no estrecharla
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF84240C) else Color(0xFFF5E6CC) // El color crema de tu captura
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp, // Sombra mucho más marcada
            pressedElevation = 15.dp
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            // Portada
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color(0xFFD32F2F))
            )

            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp) // Bottom a 0
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyLarge,
                    color = authorColor,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = book.publisher,
                    style = MaterialTheme.typography.bodySmall,
                    color = titleColor
                )

                Spacer(modifier = Modifier.weight(1f))

                // FILA DE ICONOS SIN MARGEN INFERIOR
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp), // Tú controlas exactamente cuánto espacio queda abajo
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (book.isFavorite) Color(0xFFFF0000) else titleColor
                            )
                        }
                        IconButton(onClick = onBookmarkClick) {
                            Icon(
                                imageVector = if (book.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = if (book.isBookmarked) Color(0xFF008CFF) else titleColor
                            )
                        }
                        IconButton(onClick = onReturnClick) {
                            Icon(
                                imageVector = if (book.isToReturn) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                                contentDescription = null,
                                tint = if (book.isToReturn) Color(0xFFFFB700) else titleColor
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.example.pmdm.archivebook.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.presentation.LibraryViewModel
import com.example.pmdm.archivebook.ui.theme.DarkOnPrimary
import com.example.pmdm.archivebook.ui.theme.DarkPrimary
import com.example.pmdm.archivebook.ui.theme.LightOnPrimary
import com.example.pmdm.archivebook.ui.theme.LightPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onLogout: () -> Unit,
    onBookClick: (Int) -> Unit,
    viewModel: LibraryViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme = isSystemInDarkTheme()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var showMenu by remember { mutableStateOf(false) }
    var showGenreMenu by remember { mutableStateOf(false) }

    val contentColor = if (isDarkTheme) Color(0xFFF5E6CC) else Color(0xFF7B241C)
    val selectedBg = if (isDarkTheme) Color(0xFF390A02) else Color(0xFFF8F2E4)

    val booksToShow = viewModel.filteredBooks
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    if (viewModel.forceLogout) {
        LaunchedEffect(Unit) { onLogout() }
    }

    val textFieldContentColor = if (isDarkTheme) DarkOnPrimary else LightOnPrimary

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxHeight()
            ) {
                Spacer(modifier = Modifier.height(WindowInsets.systemBars.asPaddingValues().calculateTopPadding()))

                Text(
                    "ArchiveBook",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = contentColor.copy(alpha = 0.2f))

                val drawerItemColors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = selectedBg,
                    unselectedContainerColor = Color.Transparent,
                    selectedIconColor = contentColor,
                    unselectedIconColor = contentColor,
                    selectedTextColor = contentColor,
                    unselectedTextColor = contentColor
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        // --- ALL LIBRARY (Reset total) ---
                        NavigationDrawerItem(
                            label = { Text("All Library") },
                            selected = viewModel.selectedCategory == "All",
                            icon = { Icon(Icons.Default.Menu, null) },
                            onClick = {
                                viewModel.resetToAll() // Limpia búsqueda y vuelve a 'All'
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = drawerItemColors,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        // --- BESTSELLERS ---
                        NavigationDrawerItem(
                            label = { Text("Bestsellers") },
                            selected = viewModel.selectedCategory == "Bestsellers",
                            icon = { Icon(Icons.Default.Star, null) },
                            onClick = {
                                viewModel.selectedCategory = "Bestsellers"
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = drawerItemColors,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        // --- FAVORITES ---
                        NavigationDrawerItem(
                            label = { Text("Favorites") },
                            selected = viewModel.selectedCategory == "Favorites",
                            icon = { Icon(Icons.Default.Favorite, null) },
                            onClick = {
                                viewModel.selectedCategory = "Favorites"
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = drawerItemColors,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        // --- YET TO READ ---
                        NavigationDrawerItem(
                            label = { Text("Yet to read") },
                            selected = viewModel.selectedCategory == "YetToRead",
                            icon = { Icon(Icons.Default.Bookmark, null) },
                            onClick = {
                                viewModel.selectedCategory = "YetToRead"
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = drawerItemColors,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )

                        // --- LOG OUT ---
                        NavigationDrawerItem(
                            label = { Text("Log Out") },
                            selected = false,
                            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, null) },
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                    onLogout()
                                }
                            },
                            colors = drawerItemColors,
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 2.dp,
                    modifier = Modifier.padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Abrir menú", tint = contentColor)
                        }

                        TextField(
                            value = if (viewModel.selectedFilter == "Genre") viewModel.selectedGenres.joinToString(", ") else viewModel.searchText,
                            onValueChange = { if (viewModel.selectedFilter != "Genre") viewModel.searchText = it },
                            readOnly = (viewModel.selectedFilter == "Genre"),
                            placeholder = {
                                val label = if (viewModel.selectedFilter == "Genre") "Select genres..." else "Search by ${viewModel.selectedFilter}"
                                Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textFieldContentColor)
                            },
                            modifier = Modifier.weight(1f).height(54.dp).padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                Icon(
                                    imageVector = if (viewModel.selectedFilter == "Genre") Icons.Default.Mode else Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = textFieldContentColor
                                )
                            },
                            trailingIcon = {
                                if (viewModel.searchText.isNotEmpty() || viewModel.selectedGenres.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.clearFilters() }) {
                                        Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp), tint = textFieldContentColor)
                                    }
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = if (isDarkTheme) DarkPrimary else LightPrimary,
                                unfocusedContainerColor = if (isDarkTheme) DarkPrimary else LightPrimary,
                                focusedTextColor = textFieldContentColor,
                                unfocusedTextColor = textFieldContentColor,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, "filters", tint = contentColor)
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = {
                                    showGenreMenu = false
                                    showMenu = false

                                    if (viewModel.selectedGenres.isEmpty()) {
                                        viewModel.selectedFilter = "Title"
                                    }
                                },
                                containerColor = if (isDarkTheme) DarkPrimary else LightPrimary
                            ) {
                                val filters = listOf(
                                    "Title" to Icons.Default.Book,
                                    "Author" to Icons.Default.Person,
                                    "Publisher" to Icons.Default.Apartment,
                                    "Genre" to Icons.Default.Mode
                                )

                                filters.forEach { (name, icon) ->
                                    DropdownMenuItem(
                                        text = { Text(text = name, color = textFieldContentColor) },
                                        onClick = {
                                            viewModel.selectedFilter = name
                                            if (name == "Genre") {
                                                showMenu = false
                                                showGenreMenu = true
                                            } else {
                                                viewModel.clearFilters()
                                                showMenu = false
                                            }
                                        },
                                        leadingIcon = { Icon(icon, null, tint = textFieldContentColor) }
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showGenreMenu,
                                onDismissRequest = {
                                    showGenreMenu = false
                                    // Si no seleccionó nada, volvemos a Title para no bloquear el teclado
                                    if (viewModel.selectedGenres.isEmpty()) viewModel.selectedFilter = "Title"
                                },
                                containerColor = if (isDarkTheme) DarkPrimary else LightPrimary
                            ) {
                                val genres = listOf("Fantasy", "Terror", "Sci-Fi", "Romance", "History", "Clásico", "Distopía")
                                genres.forEach { genre ->
                                    val isChecked = viewModel.selectedGenres.contains(genre)
                                    DropdownMenuItem(
                                        text = { Text(text = genre, color = if (isDarkTheme) LightPrimary else DarkPrimary) },
                                        onClick = { viewModel.toggleGenre(genre) },
                                        leadingIcon = {
                                            Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = null,
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = if (isDarkTheme) LightPrimary else DarkPrimary,
                                                    checkmarkColor = if (isDarkTheme) DarkPrimary else LightPrimary,
                                                    uncheckedColor = if (isDarkTheme) LightPrimary else DarkPrimary
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

            // --- CONTENIDO PRINCIPAL ---
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = contentColor)
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Error: $errorMessage", modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
            } else if (booksToShow.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("No books found matching your criteria.", modifier = Modifier.align(Alignment.Center), color = contentColor)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(booksToShow, key = { it.id }) { book ->
                        BookCard(
                            book = book,
                            modifier = Modifier.clickable { onBookClick(book.id) },
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

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onReturnClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val titleColor = if (isDark) Color(0xFFF5E6CC) else Color(0xFF84240C)
    val authorColor = if (isDark) Color(0xFFFFFFFF) else Color(0xFF000000)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF84240C) else Color(0xFFF5E6CC)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(180.dp)) {
            // Portada (Placeholder)
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color(0xFFD32F2F))
            ) {
                // Aquí podrías poner una AsyncImage de Coil si tu API devuelve URLs
            }

            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = authorColor,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = book.publisher,
                    style = MaterialTheme.typography.labelSmall,
                    color = titleColor
                )

                Spacer(modifier = Modifier.weight(1f))

                // Fila de acciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (book.isBestseller) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Bestseller",
                        tint = if (book.isBestseller) Color(0xFFFFB700) else titleColor
                    )
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (book.isFavorite) Color(0xFFFF0000) else titleColor
                        )
                    }
                    IconButton(onClick = onBookmarkClick) {
                        Icon(
                            imageVector = if (book.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (book.isBookmarked) Color(0xFF008CFF) else titleColor
                        )
                    }
                    IconButton(onClick = onReturnClick) {
                        Icon(
                            imageVector = if (book.isToReturn) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = "Return",
                            tint = if (book.isToReturn) Color(0xFF00D400) else titleColor
                        )
                    }
                }
            }
        }
    }
}

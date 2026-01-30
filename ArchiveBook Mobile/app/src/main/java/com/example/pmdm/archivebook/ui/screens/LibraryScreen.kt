package com.example.pmdm.archivebook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pmdm.archivebook.domain.Book
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    books: List<Book>? = null, onLogout: () -> Unit, errorMessage: String? = null
) {
    // ESTADOS
    var searchText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

    var showMenu by remember { mutableStateOf(false) }
    var showGenreMenu by remember { mutableStateOf(false) }

    var selectedFilter by remember { mutableStateOf("Title") }
    var selectedGenres by remember { mutableStateOf(setOf<String>()) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedBg = if (isDarkTheme) Color(0xFF390A02) else Color(0xFFF8F2E4) // Este es el fondo claro para el ítem seleccionado
    val contentColor = if (isDarkTheme) Color(0xFFF5E6CC) else Color(0xFF7B241C) // Color granate/oscuro

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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Abrir menú", tint = contentColor)
                        }

                        TextField(
                            value = searchText,
                            onValueChange = { if (selectedFilter != "Genre") searchText = it }, // Bloqueamos escritura si es Genre
                            readOnly = selectedFilter == "Genre", // <--- EVITA QUE SALGA EL TECLADO
                            placeholder = {
                                val placeholderText = if (selectedFilter == "Genre") {
                                    if (selectedGenres.isEmpty()) "Select genres in menu..."
                                    else selectedGenres.joinToString(", ")
                                } else {
                                    "Search by $selectedFilter"
                                }
                                Text(
                                    text = placeholderText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.7f),
                                    maxLines = 1
                                )
                            },
                            modifier = Modifier.weight(1f).height(54.dp).padding(horizontal = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            leadingIcon = {
                                // CAMBIO DE ICONO: Si es género, ponemos el icono de modo/filtro
                                Icon(
                                    imageVector = if (selectedFilter == "Genre") Icons.Default.Mode else Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            },
                            trailingIcon = {
                                // El botón de cerrar ahora limpia datos Y restablece el filtro a Title
                                if (searchText.isNotEmpty() || selectedGenres.isNotEmpty()) {
                                    IconButton(onClick = {
                                        searchText = ""
                                        selectedGenres = emptySet()
                                        selectedFilter = "Title" // <--- Agregamos esto para que deje de ser "Genre"
                                    }) {
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
                                focusedTextColor = MaterialTheme.colorScheme.background,
                                unfocusedTextColor = MaterialTheme.colorScheme.background,
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
                                    val isSelected = selectedFilter == name
                                    Surface(
                                        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                            onClick = {
                                                if (name == "Genre") {
                                                    showGenreMenu = true
                                                    // No cerramos showMenu aquí para que el submenú se apoye en él
                                                } else {
                                                    selectedFilter = name
                                                    selectedGenres = emptySet()
                                                    showMenu = false
                                                }
                                            },
                                            leadingIcon = { Icon(icon, null, modifier = Modifier.size(18.dp)) },
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
                                onDismissRequest = {
                                    showGenreMenu = false
                                    showMenu = false // <--- CLAVE: Al tocar fuera del submenú, cerramos el principal también
                                },
                                modifier = Modifier.background(contentColor)
                            ) {
                                val genres = listOf("Fantasy", "Terror", "Sci-Fi", "Romance", "History")
                                genres.forEach { genre ->
                                    val isChecked = selectedGenres.contains(genre)
                                    DropdownMenuItem(
                                        text = { Text(genre, color = MaterialTheme.colorScheme.surface) },
                                        onClick = {
                                            val newSelectedGenres = if (isChecked) selectedGenres - genre else selectedGenres + genre
                                            selectedGenres = newSelectedGenres

                                            if (newSelectedGenres.isEmpty()) {
                                                selectedFilter = "Title"
                                                showGenreMenu = false
                                                showMenu = false
                                            } else {
                                                selectedFilter = "Genre"
                                                searchText = ""
                                            }
                                        },
                                        leadingIcon = {
                                            androidx.compose.material3.Checkbox(
                                                checked = isChecked,
                                                onCheckedChange = null,
                                                colors = androidx.compose.material3.CheckboxDefaults.colors(
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
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                val filteredBooks = books?.filter { book ->
                    if (selectedFilter == "Genre") {
                        selectedGenres.isEmpty() || selectedGenres.contains(book.genre)
                    } else {
                        if (searchText.isEmpty()) true
                        else {
                            when (selectedFilter.trim().lowercase()) {
                                "title" -> book.title.contains(searchText, ignoreCase = true)
                                "author" -> book.author.contains(searchText, ignoreCase = true)
                                "publisher" -> book.publisher.contains(searchText, ignoreCase = true)
                                else -> true
                            }
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(errorMessage, Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)
                } else if (filteredBooks.isNullOrEmpty()) {
                    Text("There are no books available.", Modifier.align(Alignment.Center), color = contentColor)
                } else {
                    LazyColumn(state = listState, contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(filteredBooks) { book -> BookCard(book) }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(book: Book) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            //Portada del libro
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(Color(0xFFD32F2F)) //Imagen
            )

            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .padding(12.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = book.publisher,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val iconTint = MaterialTheme.colorScheme.onSurfaceVariant

                    Icon(
                        imageVector = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 12.dp),
                        tint = if (book.isFavorite) Color.Red else iconTint
                    )

                    Icon(
                        imageVector = if (book.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 12.dp),
                        tint = iconTint
                    )

                    Icon(
                        imageVector = if (book.isToReturn) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = null,
                        tint = if (book.isToReturn) MaterialTheme.colorScheme.error else iconTint
                    )
                }
            }
        }
    }
}
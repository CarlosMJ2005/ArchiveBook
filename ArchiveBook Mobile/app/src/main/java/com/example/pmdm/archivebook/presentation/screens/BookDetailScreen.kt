package com.example.pmdm.archivebook.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.presentation.BookDetailViewModel
import com.example.pmdm.archivebook.utils.ApiConstants

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val contentColor = if (isDark) Color(0xFFF5E6CC) else Color(0xFF7B241C)

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = contentColor)
        }
    } else if (uiState.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = uiState.error!!, color = contentColor)
        }
    } else if (uiState.book != null) {
        // PASAMOS LAS ACCIONES DEL VIEWMODEL AL CONTENIDO
        BookDetailContent(
            book = uiState.book!!,
            onBack = onBack,
            token = uiState.token ?: "",
            onFavoriteClick = { viewModel.toggleFavorite() },
            onBookmarkClick = { viewModel.toggleBookmark() },
            onBorrowClick = { viewModel.borrowBook() },
            onReturnClick = { viewModel.returnBook() }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookDetailContent(
    book: Book,
    onBack: () -> Unit,
    token: String,
    onFavoriteClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onBorrowClick: () -> Unit,
    onReturnClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val contentColor = if (isDark) Color(0xFFF5E6CC) else Color(0xFF7B241C)
    val cardColor = if (isDark) Color(0xFF84240C) else Color(0xFFF5E6CC)
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = contentColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = contentColor
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // PORTADA DEL LIBRO
            Card(
                modifier = Modifier.size(width = 200.dp, height = 300.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                val context = androidx.compose.ui.platform.LocalContext.current

                val token = token

                val imageRequest = coil.request.ImageRequest.Builder(context)
                    .data(ApiConstants.getCoverUrl(book.id))
                    .addHeader("Authorization", "Bearer $token") // Esto autoriza la descarga
                    .crossfade(true)
                    .build()

                AsyncImage(
                    model = imageRequest,
                    contentDescription = "Portada de ${book.title}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Default.Book),
                    placeholder = rememberVectorPainter(Icons.Default.CloudDownload)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // TEXTOS DE INFORMACIÓN
            Text(book.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = contentColor, textAlign = TextAlign.Center)
            Text(book.author, style = MaterialTheme.typography.titleLarge, fontStyle = FontStyle.Italic, color = textColor.copy(alpha = 0.8f))
            Text("Published by ${book.publisher}", style = MaterialTheme.typography.bodyMedium, color = contentColor.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(20.dp))

            // --- FILA ÚNICA DE ICONOS (Arreglada) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Informativo
                StatusBadge(
                    icon = if (book.isBestseller) Icons.Default.Star else Icons.Default.StarBorder,
                    label = "Bestseller",
                    activeColor = Color(0xFFFFB700),
                    inactiveColor = contentColor, // <--- CAMBIO AQUÍ
                    isActive = book.isBestseller
                )

                // Botón Favorito
                ClickableStatusBadge(
                    icon = if (book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    label = "Favorite",
                    activeColor = Color(0xFFFF0000),
                    inactiveColor = contentColor, // <--- CAMBIO AQUÍ
                    isActive = book.isFavorite,
                    onClick = onFavoriteClick
                )

                // Botón Marcador
                ClickableStatusBadge(
                    icon = if (book.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    label = "Reading",
                    activeColor = Color(0xFF008CFF),
                    inactiveColor = contentColor, // <--- CAMBIO AQUÍ
                    isActive = book.isBookmarked,
                    onClick = onBookmarkClick
                )

                // Botón Préstamo/Devolución
                ClickableStatusBadge(
                    icon = if (book.isToReturn) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                    label = if (book.isToReturn) "Return" else "Borrow",
                    activeColor = Color(0xFF0FDC0F),
                    inactiveColor = contentColor, // <--- CAMBIO AQUÍ
                    isActive = book.isToReturn,
                    onClick = {
                        if (book.isToReturn) onReturnClick() else onBorrowClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // SINOPSIS
            Surface(
                color = cardColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Synopsis", fontWeight = FontWeight.Bold, color = contentColor, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(book.synopsis, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp, color = textColor, textAlign = TextAlign.Justify)
                }
            }
        }
    }
}

@Composable
fun ClickableStatusBadge(
    icon: ImageVector,
    label: String,
    activeColor: Color,
    inactiveColor: Color, // <--- NUEVO PARÁMETRO
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(75.dp)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            // Si no está activo, usa el color del título con un poco de transparencia
            tint = if (isActive) activeColor else inactiveColor.copy(alpha = 0.6f),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            ),
            color = if (isActive) activeColor else inactiveColor.copy(alpha = 0.6f),
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun StatusBadge(
    icon: ImageVector,
    label: String,
    activeColor: Color,
    inactiveColor: Color,
    isActive: Boolean
) {
    Column(
        modifier = Modifier.width(75.dp), // Añadido para consistencia
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) activeColor else inactiveColor.copy(alpha = 0.6f),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = if (isActive) activeColor else inactiveColor.copy(alpha = 0.6f)
        )
    }
}
package com.example.pmdm.archivebook.presentation.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pmdm.archivebook.di.BookDetailViewModelFactory
import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.presentation.BookDetailViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    factory: BookDetailViewModelFactory,
    onBack: () -> Unit,
    viewModel: BookDetailViewModel = viewModel(factory = factory)
) {
    val book = viewModel.book
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    val isDark = isSystemInDarkTheme()
    val contentColor = if (isDark) Color(0xFFF5E6CC) else Color(0xFF7B241C)

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = contentColor)
            }
        }
        error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = error, color = contentColor)
            }
        }
        book != null -> {
            // Esta es la función que faltaba declarar abajo
            BookDetailContent(book = book, onBack = onBack)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BookDetailContent(
    book: Book,
    onBack: () -> Unit
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
            // --- PORTADA ---
            Card(
                modifier = Modifier.size(width = 180.dp, height = 270.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFFD32F2F)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Book, null, modifier = Modifier.size(80.dp), tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INFO PRINCIPAL ---
            Text(book.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = contentColor, textAlign = TextAlign.Center)
            Text(book.author, style = MaterialTheme.typography.titleLarge, fontStyle = FontStyle.Italic, color = textColor.copy(alpha = 0.8f))
            Text("Published by ${book.publisher}", style = MaterialTheme.typography.bodyMedium, color = contentColor.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(20.dp))

            // --- ESTADOS (BADGES) ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                StatusBadge(Icons.Default.Star, "Bestseller", Color(0xFFFFB700), book.isBestseller)
                StatusBadge(Icons.Default.Favorite, "Favorite", Color(0xFFFF0000), book.isFavorite)
                StatusBadge(Icons.Default.Bookmark, "Reading", Color(0xFF008CFF), book.isBookmarked)
                StatusBadge(Icons.Default.NotificationsActive, "To Return", Color(0xFF0FDC0F), book.isToReturn)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- GÉNEROS ---
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                book.genres.forEach { genre ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(genre) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            labelColor = contentColor,
                            containerColor = cardColor.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- SINOPSIS ---
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
fun StatusBadge(icon: ImageVector, label: String, activeColor: Color, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) activeColor else Color.Gray.copy(alpha = 0.4f),
            modifier = Modifier.size(30.dp)
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = if (isActive) activeColor else Color.Gray.copy(alpha = 0.4f))
    }
}
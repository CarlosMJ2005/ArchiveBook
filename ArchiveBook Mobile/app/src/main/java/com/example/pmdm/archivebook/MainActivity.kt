package com.example.pmdm.archivebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.example.pmdm.archivebook.data.local.AuthManager
import com.example.pmdm.archivebook.presentation.navigationroot.NavigationRoot
import com.example.pmdm.archivebook.ui.theme.ArchiveBookTheme
import okhttp3.OkHttpClient
import org.koin.android.ext.android.inject

// Implementamos ImageLoaderFactory para que Coil use esta configuración globalmente
class MainActivity : ComponentActivity(), ImageLoaderFactory {

    // Inyectamos el AuthManager para obtener el token
    private val authManager: AuthManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchiveBookTheme {
                NavigationRoot()
            }
        }
    }

    // Esta función configura Coil para TODA la aplicación
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .okHttpClient {
                // Creamos un cliente OkHttp que añade el Header automáticamente
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val token = authManager.getToken() ?: ""
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        chain.proceed(newRequest)
                    }
                    .build()
            }
            .respectCacheHeaders(false) // A veces ayuda si el servidor tiene headers de caché extraños
            .build()
    }
}
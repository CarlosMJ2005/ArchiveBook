package com.example.pmdm.archivebook.data.remote

import android.util.Log
import com.example.pmdm.archivebook.data.BookDto
import com.example.pmdm.archivebook.data.PrestamoDto
import com.example.pmdm.archivebook.data.local.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put

class LibraryApiService(
    private val client: HttpClient,
    private val authManager: AuthManager
) {
    private val TAG = "API_libros"

    private fun HttpRequestBuilder.withAuth() {
        authManager.getToken()?.let { token ->
            header("Authorization", "Bearer $token")
        }
    }

    // --- GET --- //
    suspend fun fetchBooks(): List<BookDto> = client.get("api/libros") { withAuth() }.body()

    suspend fun getLoans(): List<PrestamoDto> {
        Log.d(TAG, "Préstamos -> GET: api/prestamos")
        return client.get("api/prestamos") { withAuth() }.body()
    }

    // --- FAVORITES --- //
    suspend fun addToFavorites(bookId: Int): Boolean {
        Log.d(TAG, "Favorito -> POST: api/libros/$bookId/favorite")
        val response = client.post("api/libros/$bookId/favorite") { withAuth() }
        return response.status.value in 200..299
    }

    suspend fun removeFromFavorites(bookId: Int): Boolean {
        Log.d(TAG, "Favorito -> DELETE: api/libros/$bookId/favorite")
        val response = client.delete("api/libros/$bookId/favorite") { withAuth() }
        return response.status.value in 200..299
    }

    // --- BOOKMARKS --- //
    suspend fun addBookmark(bookId: Int): Boolean {
        Log.d(TAG, "Bookmark -> POST: api/libros/$bookId/bookmark")
        val response = client.post("api/libros/$bookId/bookmark") { withAuth() }
        return response.status.value in 200..299
    }

    suspend fun removeBookmark(bookId: Int): Boolean {
        Log.d(TAG, "Bookmark -> DELETE: api/libros/$bookId/bookmark")
        val response = client.delete("api/libros/$bookId/bookmark") { withAuth() }
        return response.status.value in 200..299
    }

    // --- RETURNS --- //
    suspend fun markToReturn(bookId: Int): Boolean {
        val response = client.put("api/prestamos/$bookId/devolver") { withAuth() }
        return response.status.value in 200..299
    }

    suspend fun cancelReturn(bookId: Int): Boolean {
        val response = client.delete("api/prestamos/$bookId") { withAuth() }
        return response.status.value in 200..299
    }
}

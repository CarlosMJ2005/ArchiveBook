package com.example.pmdm.archivebook.data.remote

import android.util.Log
import com.example.pmdm.archivebook.data.BookDto
import com.example.pmdm.archivebook.data.local.AuthManager
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
class LibraryApiService(
    private val client: HttpClient,
    private val authManager: AuthManager // Esto quita el error de la imagen 899901
) {
    private val TAG = "API_libros"

    // Esta función dentro de la clase quita el error de la imagen 8995a1
    private fun HttpRequestBuilder.withAuth() {
        authManager.getToken()?.let { token ->
            header("Authorization", "Bearer $token")
        }
    }
    // --- GET ---
    suspend fun fetchBooks(): List<BookDto> = client.get("api/libros").body()

    // --- FAVORITES ---
    suspend fun addToFavorites(bookId: Int): Boolean {
        Log.d(TAG, "Favorito -> POST: api/libros/$bookId/favorite")
        val response = client.post("api/libros/$bookId/favorite")
        return response.status.value == 200
    }

    suspend fun removeFromFavorites(bookId: Int): Boolean {
        Log.d(TAG, "Favorito -> DELETE: api/libros/$bookId/favorite")
        val response = client.delete("api/libros/$bookId/favorite")
        return response.status.value == 200
    }

    // --- BOOKMARKS ---
    suspend fun addBookmark(bookId: Int): Boolean {
        Log.d(TAG, "Bookmark -> POST: api/libros/$bookId/bookmark")
        return client.post("api/libros/$bookId/bookmark").status.value == 200
    }

    suspend fun removeBookmark(bookId: Int): Boolean {
        Log.d(TAG, "Bookmark -> DELETE: api/libros/$bookId/bookmark")
        return client.delete("api/libros/$bookId/bookmark").status.value == 200
    }

    // --- RETURNS (MÉTODOS CORREGIDOS) ---

    suspend fun markToReturn(bookId: Int): Boolean { // Eliminamos bookDto de los parámetros
        return try {
            // Añadimos "/" al inicio para asegurar que la ruta sea absoluta desde la baseUrl
            val response = client.put("/api/prestamos/$bookId/devolver") {
                // Eliminamos contentType y setBody porque NO envías datos en el cuerpo
                withAuth()
            }

            // Log detallado para depuración
            Log.d(TAG, "Llamando a: ${response.call.request.url}")
            Log.d(TAG, "Resultado markToReturn: Status = ${response.status.value} (${response.status.description})")

            // Verificamos si la respuesta es exitosa (200 OK, 201 Created o 204 No Content)
            response.status.value in 200..204
        } catch (e: Exception) {
            Log.e(TAG, "Error fatal en markToReturn: ${e.message}")
            false
        }
    }

    suspend fun cancelReturn(bookId: Int): Boolean {
        return try {
            val response = client.delete("api/prestamos/$bookId") {
                withAuth() // <--- Esto soluciona el 403
            }
            response.status.value in 200..204
        } catch (e: Exception) {
            false
        }
    }
}
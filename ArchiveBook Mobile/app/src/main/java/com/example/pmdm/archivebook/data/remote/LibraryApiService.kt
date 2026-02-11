package com.example.pmdm.archivebook.data.remote

import com.example.pmdm.archivebook.data.BookDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class LibraryApiService(private val client: HttpClient) {

    // GET: El backend ya debería devolverte el is_favorite: true/false en el JSON
    suspend fun fetchBooks(): List<BookDto> = client.get("api/libros").body()

    // POST: Crear la relación en la tabla intermedia
    suspend fun addToFavorites(bookId: Int): Boolean {
        // Asumiendo que el token de usuario va en el Header automáticamente
        val response = client.post("api/libros/$bookId/favorite")
        return response.status.value == 200
    }

    // DELETE: Borrar la relación
    suspend fun removeFromFavorites(bookId: Int): Boolean {
        val response = client.delete("api/libros/$bookId/favorite")
        return response.status.value == 200 // o 204 No Content
    }

    suspend fun addBookmark(bookId: Int): Boolean {
        return client.post("api/libros/$bookId/bookmark").status.value == 200
    }

    // DELETE: Lo quita de la lista
    suspend fun removeBookmark(bookId: Int): Boolean {
        return client.delete("api/libros/$bookId/bookmark").status.value == 200
    }
}
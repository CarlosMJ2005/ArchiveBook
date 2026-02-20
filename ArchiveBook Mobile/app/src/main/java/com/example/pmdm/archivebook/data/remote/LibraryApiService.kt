package com.example.pmdm.archivebook.data.remote

import android.util.Log
import com.example.pmdm.archivebook.data.BookDto
import com.example.pmdm.archivebook.data.BookmarkDto
import com.example.pmdm.archivebook.data.FavoritoDto
import com.example.pmdm.archivebook.data.PrestamoDto
import com.example.pmdm.archivebook.data.local.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LibraryApiService(
    private val client: HttpClient,
    private val authManager: AuthManager
) {
    private val TAG = "API_libros"

    // Todos los get
    suspend fun fetchBooks(): List<BookDto> = client.get("api/libros").body()

    suspend fun getLoans(): List<PrestamoDto> {
        Log.d(TAG, "Préstamos -> GET: api/prestamos")
        return client.get("api/prestamos").body()
    }

    suspend fun getFavorites(): List<FavoritoDto> { // Cambiado de List<BookDto>
        val token = authManager.getToken()
        return client.get("api/favoritos") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun getBookmarks(): List<BookmarkDto> { // Cambiado de List<BookDto>
        val token = authManager.getToken()
        return client.get("api/porLeer") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    // FAVORITOS
    suspend fun addToFavorites(bookId: Int): Boolean {
        val token = authManager.getToken()
        Log.d(TAG, "Favorito -> POST: api/favoritos/$bookId")

        val response = client.post("api/favoritos/$bookId") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody("{}") // Cuerpo vacío para evitar errores de Content-Length
        }
        return response.status.value in 200..299
    }

    suspend fun removeFromFavorites(bookId: Int): Boolean {
        val token = authManager.getToken()
        Log.d(TAG, "Favorito -> DELETE: api/favoritos/$bookId")

        val response = client.delete("api/favoritos/$bookId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.status.value in 200..299
    }

    // POR LEER
    suspend fun addBookmark(bookId: Int): Boolean {
        val token = authManager.getToken()
        Log.d(TAG, "Bookmark -> POST: api/porLeer/$bookId")

        val response = client.post("api/porLeer/$bookId") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody("{}")
        }
        return response.status.value in 200..299
    }

    suspend fun removeBookmark(bookId: Int): Boolean {
        val token = authManager.getToken()
        Log.d(TAG, "Bookmark -> DELETE: api/porLeer/$bookId")

        val response = client.delete("api/porLeer/$bookId") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        return response.status.value in 200..299
    }

    // --- LOANS (Préstamos) --- //
    suspend fun borrowBook(bookId: Int): Boolean {
        val token = authManager.getToken()

        val requestBody = PrestamoCreationRequest(
            libro = BookIdRequest(id = bookId),
            returnDate = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ISO_DATE)
        )

        Log.d(TAG, "Reserva -> POST: api/prestamos | Token: ${token?.take(10)}...")

        val response = client.post("api/prestamos") {
            // Inyectamos el token manualmente para asegurar que no falle por el plugin Auth
            header(HttpHeaders.Authorization, "Bearer $token")

            // Enviamos el objeto serializado
            setBody(requestBody)
        }

        return response.status.value in 200..299
    }

    suspend fun returnBook(bookId: Int): Boolean {
        // Usamos 'authManager' (la instancia del constructor), no 'AuthManager' (la clase)
        val token = authManager.getToken()

        Log.d(TAG, "Inyección MANUAL de token: $token")

        val response = client.patch("api/prestamos/devolver/$bookId") {
            // Añadimos el header manualmente
            header(HttpHeaders.Authorization, "Bearer $token")
            // Enviamos un cuerpo vacío para evitar que el backend rechace la petición PATCH
            setBody("{}")
        }

        return response.status.value in 200..299
    }

    suspend fun deleteLoan(bookId: Int): Boolean { // Formerly cancelReturn
        Log.d(TAG, "Préstamo -> DELETE: api/prestamos/$bookId")
        val response = client.delete("api/prestamos/$bookId")
        return response.status.value in 200..299
    }
}

// --- Data classes for POST request ---
@Serializable
private data class PrestamoCreationRequest(
    val libro: BookIdRequest,
    @SerialName("fechaDevolucionPrevista")
    val returnDate: String
)

@Serializable
private data class BookIdRequest(
    @SerialName("idLibro")
    val id: Int
)

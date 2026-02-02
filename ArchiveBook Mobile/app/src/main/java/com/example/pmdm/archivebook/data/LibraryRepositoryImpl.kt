package com.example.pmdm.archivebook.data

import com.example.pmdm.archivebook.domain.Book
import com.example.pmdm.archivebook.domain.repositories.LibraryRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class LibraryRepositoryImpl(private val client: HttpClient) : LibraryRepository {

    // Ponemos los datos aquí como una propiedad privada
    private val mockBooks = listOf(
        Book(
            id = 1,
            title = "El Imperio Final",
            author = "Brandon Sanderson",
            publisher = "Nova",
            genres = listOf("Fantasía", "Épico", "Aventura"),
            isFavorite = true
        ),
        Book(
            id = 2,
            title = "Cien años de soledad y la gran puta que me pario me cago en mi madre",
            author = "Gabriel García Márquez",
            publisher = "Sudamericana",
            genres = listOf("Realismo Mágico", "Clásico"),
            isBookmarked = true
        ),
        Book(
            id = 3,
            title = "1984",
            author = "George Orwell",
            publisher = "Secker & Warburg",
            genres = listOf("Distopía", "Ficción Política")
        )
    )

    override suspend fun getBooks(): Result<List<Book>> {
        return try {
            // Simulamos un retraso de red para ver el indicador de carga (opcional)
            // kotlinx.coroutines.delay(1000)

            // Por ahora devolvemos la lista de mock en lugar de llamar a client.get(...)
            Result.success(mockBooks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
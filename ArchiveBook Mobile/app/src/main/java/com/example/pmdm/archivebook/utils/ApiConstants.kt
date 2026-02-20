package com.example.pmdm.archivebook.utils

object ApiConstants{
    const val BASE_URL = "http://192.168.0.12:8080/"

    fun getCoverUrl(bookId: Int): String {
        return "${BASE_URL}api/libros/$bookId/portada"
    }
}
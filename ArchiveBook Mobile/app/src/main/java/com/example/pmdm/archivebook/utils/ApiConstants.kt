package com.example.pmdm.archivebook.utils

object ApiConstants{
    const val BASE_URL = "http://10.75.204.184:8080/"
    //192.168.0.12
    //10.75.204.184
    fun getCoverUrl(bookId: Int): String {
        return "${BASE_URL}api/libros/$bookId/portada"
    }
}
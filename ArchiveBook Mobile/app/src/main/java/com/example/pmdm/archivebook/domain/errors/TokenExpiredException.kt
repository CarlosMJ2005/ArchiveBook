package com.example.pmdm.archivebook.domain.errors

class TokenExpiredException(
    message: String = "Your session has expired. Please log in again."
) : Exception(message)

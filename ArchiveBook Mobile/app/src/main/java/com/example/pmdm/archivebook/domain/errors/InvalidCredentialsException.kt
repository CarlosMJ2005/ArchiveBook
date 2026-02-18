package com.example.pmdm.archivebook.domain.errors

class InvalidCredentialsException(message: String = "The email or password you entered is incorrect.") : Exception(message)

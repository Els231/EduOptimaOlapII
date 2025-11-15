package com.example.eduoptimaolapii.data.model.transaccional

// Estos se mantienen para compatibilidad si los necesitas
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: UsuarioSimple,
    val mensaje: String = ""
)



data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

data class RegisterResponse(
    val mensaje: String,
    val usuario: UsuarioSimple
)
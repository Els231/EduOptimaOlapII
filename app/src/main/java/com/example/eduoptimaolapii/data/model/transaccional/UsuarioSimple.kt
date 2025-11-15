package com.example.eduoptimaolapii.data.model.transaccional

import androidx.annotation.Keep

@Keep
data class UsuarioSimple(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: String,
    val fechaCreacion: String? = null,
    val estado: String = "Activo"
)

@Keep
data class RefreshTokenRequest(
    val refreshToken: String
)

@Keep
data class RefreshTokenResponse(
    val token: String,
    val refreshToken: String,
    val usuario: UsuarioSimple
)

@Keep
data class LogoutRequest(
    val token: String
)

@Keep
data class LogoutResponse(
    val message: String,
    val success: Boolean = true
)
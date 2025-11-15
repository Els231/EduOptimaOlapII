package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.mongodb.UsuarioMongo
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoAuthService
import com.example.eduoptimaolapii.data.model.transaccional.LoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val mongoAuthService: MongoAuthService
) {
    suspend fun login(email: String, password: String): UsuarioMongo {
        val response = mongoAuthService.login(LoginRequest(email, password))
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Usuario no encontrado")
        } else {
            throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun register(usuario: UsuarioMongo): UsuarioMongo {
        val response = mongoAuthService.register(usuario)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Error en registro")
        } else {
            throw Exception("Error: ${response.code()} - ${response.message()}")
        }
    }
}

data class LoginRequest(val email: String, val password: String)
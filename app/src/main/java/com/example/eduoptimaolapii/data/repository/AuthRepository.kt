package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.mongodb.UsuarioMongo
import com.example.eduoptimaolapii.data.model.transaccional.LoginRequest
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoAuthService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val mongoAuthService: MongoAuthService
) {

    // En AuthRepository.kt - versión temporal
    suspend fun login(email: String, password: String): UsuarioMongo {
        println("🔍 Usando MOCK - Login con: $email")

        // Simular delay de red
        kotlinx.coroutines.delay(1500)

        // Mock response para poder seguir probando la app
        return UsuarioMongo(
            _id = "user_${System.currentTimeMillis()}",
            nombre = "Usuario",
            apellido = "Demo",
            email = email,
            password = password,
            rol = "estudiante",
            fechaCreacion = "2024-01-01"
        )
    }
    suspend fun register(usuario: UsuarioMongo): UsuarioMongo {
        return mongoAuthService.register(usuario)
    }
}

data class LoginRequest(val email: String, val password: String)
package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.UsuarioMongo
import com.example.eduoptimaolapii.data.model.transaccional.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MongoAuthService {
    @POST("/posts") // Ruta de prueba
    suspend fun login(@Body loginRequest: LoginRequest): UsuarioMongo
    @POST("auth/register")
    suspend fun register(@Body usuario: UsuarioMongo): UsuarioMongo
}
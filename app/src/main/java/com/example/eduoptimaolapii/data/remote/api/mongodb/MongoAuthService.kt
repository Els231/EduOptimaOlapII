// File: app/src/main/java/com/example/eduoptimaolapii/data/remote/api/mongodb/MongoAuthService.kt
package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.UsuarioMongo
import com.example.eduoptimaolapii.data.model.transaccional.LoginRequest

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MongoAuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<UsuarioMongo>

    @POST("auth/register")
    suspend fun register(@Body usuario: UsuarioMongo): Response<UsuarioMongo>
}
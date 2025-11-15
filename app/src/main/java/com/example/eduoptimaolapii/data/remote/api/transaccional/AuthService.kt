package com.example.eduoptimaolapii.data.remote.api.transaccional

import com.example.eduoptimaolapii.data.model.transaccional.LoginRequest
import com.example.eduoptimaolapii.data.model.transaccional.LoginResponse
import com.example.eduoptimaolapii.data.model.transaccional.RegisterRequest
import com.example.eduoptimaolapii.data.model.transaccional.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshRequest: RefreshTokenRequest): LoginResponse

    @POST("auth/logout")
    suspend fun logout(@Body logoutRequest: LogoutRequest): LogoutResponse
}

data class RefreshTokenRequest(
    val refreshToken: String
)

data class LogoutRequest(
    val token: String
)

data class LogoutResponse(
    val message: String
)
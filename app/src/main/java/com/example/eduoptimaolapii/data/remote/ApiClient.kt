package com.example.eduoptimaolapii.data.remote

import com.example.eduoptimaolapii.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    // CLIENTE PARA MONGODB
    fun createMongoDBClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MONGODB_BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // CLIENTE PARA OLAP
    fun createOLAPClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.OLAP_BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(createLoggingInterceptor())
            .addInterceptor(createHeadersInterceptor())
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(createErrorInterceptor())
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    private fun createHeadersInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "EduOptima-Android-App/1.0")
                .addHeader("X-App-Version", BuildConfig.VERSION_NAME)
                .addHeader("X-Platform", "Android")
                .build()
            chain.proceed(request)
        }
    }

    private fun createAuthInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            // Aquí puedes añadir lógica de autenticación si es necesaria
            // Por ejemplo: agregar token JWT a las requests
            val originalRequest = chain.request()

            // TODO: Obtener token de preferencias/shared preferences
            // val token = sharedPreferences.getString("auth_token", "") ?: ""
            val token = "" // Por ahora vacío, implementar cuando tengas autenticación

            val requestBuilder = originalRequest.newBuilder()

            if (token.isNotEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private fun createErrorInterceptor(): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain ->
            try {
                val request = chain.request()
                val response = chain.proceed(request)

                if (!response.isSuccessful) {
                    throw when (response.code) {
                        400 -> Exception("❌ Solicitud incorrecta - Verifique los datos enviados")
                        401 -> Exception("🔐 No autorizado - Token inválido o expirado")
                        403 -> Exception("🚫 Acceso denegado - Sin permisos suficientes")
                        404 -> Exception("🔍 Recurso no encontrado - API no disponible")
                        408 -> Exception("⏰ Timeout - Servidor no responde")
                        500 -> Exception("⚡ Error interno del servidor")
                        502 -> Exception("🌐 Bad Gateway - Error de conexión intermedia")
                        503 -> Exception("🛠️ Servicio no disponible - Intente más tarde")
                        504 -> Exception("⏱️ Gateway Timeout - Servidor tardó demasiado")
                        else -> Exception("❌ Error ${response.code}: ${response.message}")
                    }
                }
                response
            } catch (e: Exception) {
                throw when {
                    e.message?.contains("Unable to resolve host", ignoreCase = true) == true ->
                        Exception("🌐 Error de conexión. Verifique su internet")

                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        Exception("⏰ Timeout. Las APIs no responden")

                    e.message?.contains("network", ignoreCase = true) == true ->
                        Exception("📡 Error de red. Verifique su conexión")

                    e.message?.contains("SSL", ignoreCase = true) == true ->
                        Exception("🔒 Error de seguridad SSL. Verifique la fecha/hora del dispositivo")

                    e.message?.contains("closed", ignoreCase = true) == true ->
                        Exception("🔌 Conexión cerrada inesperadamente")

                    else -> Exception("❌ Error de conexión: ${e.message ?: "Desconocido"}")
                }
            }
        }
    }

    // Métodos de conveniencia para crear servicios
    inline fun <reified T> createMongoDBService(): T {
        return createMongoDBClient().create(T::class.java)
    }

    inline fun <reified T> createOLAPService(): T {
        return createOLAPClient().create(T::class.java)
    }

    // Método para verificar conectividad básica
    fun getBaseUrls(): Map<String, String> {
        return mapOf(
            "MongoDB" to BuildConfig.MONGODB_BASE_URL,
            "OLAP" to BuildConfig.OLAP_BASE_URL
        )
    }
}

// Extensión para manejar respuestas de manera más segura
suspend fun <T> safeApiCall(apiCall: suspend () -> retrofit2.Response<T>): Result<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(Exception("Respuesta vacía del servidor"))
            }
        } else {
            Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// Clase helper para estados de carga
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}
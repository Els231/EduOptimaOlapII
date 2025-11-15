package com.example.eduoptimaolapii.data.remote.api

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
            .addInterceptor(createErrorInterceptor())
            .addInterceptor(createAuthInterceptor())
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
            val token = "" // Obtener de SharedPreferences o DataStore

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
                        400 -> Exception("Solicitud incorrecta")
                        401 -> Exception("No autorizado - Token inválido o expirado")
                        403 -> Exception("Acceso denegado")
                        404 -> Exception("Recurso no encontrado")
                        408 -> Exception("Timeout de la solicitud")
                        500 -> Exception("Error interno del servidor")
                        502 -> Exception("Bad Gateway")
                        503 -> Exception("Servicio no disponible")
                        else -> Exception("Error ${response.code}: ${response.message}")
                    }
                }
                response
            } catch (e: Exception) {
                throw when {
                    e.message?.contains("Unable to resolve host") == true ->
                        Exception("❌ Error de conexión. Verifique su internet")
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        Exception("⏰ Timeout. Las APIs no responden")
                    e.message?.contains("network", ignoreCase = true) == true ->
                        Exception("🌐 Error de red. Verifique su conexión")
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
}
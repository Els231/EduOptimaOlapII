package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.NotaMongo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MongoNotaService {

    @GET("notas")
    suspend fun getNotas(): Response<List<NotaMongo>>

    @GET("notas/por-estudiante")
    suspend fun getNotasPorEstudiante(@Query("estudianteId") estudianteId: String): Response<List<NotaMongo>>

    @GET("notas/por-materia")
    suspend fun getNotasPorMateria(): Response<Map<String, Float>>

    @GET("notas/por-periodo")
    suspend fun getNotasPorPeriodo(): Response<Map<String, Float>>

    @GET("notas/promedios")
    suspend fun getPromediosGenerales(): Response<Map<String, Float>>

    @GET("notas/estadisticas")
    suspend fun getEstadisticasNotas(): Response<Map<String, Any>>

    @GET("notas/alertas")
    suspend fun getAlertasRendimiento(): Response<List<NotaMongo>>
}
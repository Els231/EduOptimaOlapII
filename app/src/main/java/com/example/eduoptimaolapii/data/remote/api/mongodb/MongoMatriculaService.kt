package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.MatriculaMongo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MongoMatriculaService {

    @GET("matriculas")
    suspend fun getMatriculas(): Response<List<MatriculaMongo>>

    @GET("matriculas/activas")
    suspend fun getMatriculasActivas(): Response<List<MatriculaMongo>>

    @GET("matriculas/por-grado")
    suspend fun getMatriculasPorGrado(): Response<Map<String, Int>>

    @GET("matriculas/por-turno")
    suspend fun getMatriculasPorTurno(): Response<Map<String, Int>>

    @GET("matriculas/por-anio")
    suspend fun getMatriculasPorAnio(): Response<Map<String, Int>>

    @GET("matriculas/estadisticas")
    suspend fun getEstadisticasMatriculas(): Response<Map<String, Any>>

    @GET("matriculas/buscar")
    suspend fun buscarMatriculas(
        @Query("query") query: String
    ): Response<List<MatriculaMongo>>
}
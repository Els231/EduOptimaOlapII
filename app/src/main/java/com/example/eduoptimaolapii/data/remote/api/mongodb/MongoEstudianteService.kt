package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MongoEstudianteService {

    // CONSULTAS PARA ESTUDIANTES
    @GET("estudiantes")
    suspend fun getEstudiantes(): Response<List<EstudianteMongo>>

    @GET("estudiantes/activos")
    suspend fun getEstudiantesActivos(): Response<List<EstudianteMongo>>

    @GET("estudiantes/por-municipio")
    suspend fun getEstudiantesPorMunicipio(): Response<Map<String, Int>>

    @GET("estudiantes/por-sexo")
    suspend fun getEstudiantesPorSexo(): Response<Map<String, Int>>

    @GET("estudiantes/por-grado")
    suspend fun getEstudiantesPorGrado(): Response<Map<String, Int>>

    @GET("estudiantes/buscar")
    suspend fun buscarEstudiantes(
        @Query("query") query: String
    ): Response<List<EstudianteMongo>>

    @GET("estudiantes/estadisticas")
    suspend fun getEstadisticasEstudiantes(): Response<Map<String, Any>>
}
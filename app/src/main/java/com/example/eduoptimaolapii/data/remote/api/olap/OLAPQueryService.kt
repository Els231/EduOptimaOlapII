package com.example.eduoptimaolapii.data.remote.api.olap

import com.example.eduoptimaolapii.data.model.olap.DimCalificacion
import com.example.eduoptimaolapii.data.model.olap.DimEstudiante
import com.example.eduoptimaolapii.data.model.olap.DimTiempo
import com.example.eduoptimaolapii.data.model.olap.FactNota
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface OLAPQueryService {

    // ✅ ENDPOINTS QUE SÍ LLEVAN "api/"
    @GET("FactNotas/Extenso")
    suspend fun getAllFactNotas(): Response<List<FactNota>>

    @GET("FactNotas/PromedioPorGrado")
    suspend fun getPromedioPorGrado(): Response<Map<String, Float>>

    @GET("FactNotas/PromedioPorTrimestre")
    suspend fun getPromedioPorTrimestre(): Response<Map<String, Float>>

    @GET("FactNotas/PromedioPorMunicipio")
    suspend fun getPromedioPorMunicipio(): Response<Map<String, Float>>

    @GET("FactNotas/Estudiante/{id}")
    suspend fun getNotasPorEstudiante(@Path("id") id: Int): Response<List<FactNota>>

    @GET("FactNotas/Promedio/Estudiante/{id}")
    suspend fun getPromedioPorEstudiante(@Path("id") id: Int): Response<Map<String, Float>>

    @GET("DimEstudiantes/DTO")
    suspend fun getAllEstudiantes(): Response<List<DimEstudiante>>

    // ✅ ENDPOINTS QUE NO LLEVAN "api/"
    @GET("DimCalificacion/DTO")
    suspend fun getAllCalificaciones(): Response<List<DimCalificacion>>

    @GET("DimTiempo/DTO")
    suspend fun getAllTiempos(): Response<List<DimTiempo>>
}
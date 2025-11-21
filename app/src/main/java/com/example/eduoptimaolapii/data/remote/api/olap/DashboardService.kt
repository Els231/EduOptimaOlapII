package com.example.eduoptimaolapii.data.remote.api.olap


import retrofit2.Response
import retrofit2.http.GET

interface DashboardService {
    // ✅ TODOS LLEVAN "api/"
    @GET("FactNotas/PromedioPorTrimestre")
    suspend fun getPromedioPorTrimestre(): Response<Map<String, Float>>

    @GET("FactNotas/PromedioPorMunicipio")
    suspend fun getPromedioPorMunicipio(): Response<Map<String, Float>>

    @GET("FactNotas/PromedioPorGrado")
    suspend fun getPromedioPorGrado(): Response<Map<String, Float>>
}
data class EventoInminente(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val fechaInicio: String,
    val fechaFinal: String,
    val grado: String
)

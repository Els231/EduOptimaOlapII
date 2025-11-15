package com.example.eduoptimaolapii.data.remote.api.olap

import com.example.eduoptimaolapii.data.model.olap.DashboardResumen
import retrofit2.Response
import retrofit2.http.GET

interface DashboardService {
    // CONSULTAS PARA VISUALIZAR DATOS EXISTENTES DEL CUBO OLAP
    @GET("dashboard/resumen")
    suspend fun getDashboardResumen(): Response<DashboardResumen>

    @GET("analytics/matriculas-por-mes")
    suspend fun getMatriculasPorMes(): Response<Map<String, Float>>

    @GET("analytics/estudiantes-por-municipio")
    suspend fun getEstudiantesPorMunicipio(): Response<Map<String, Float>>

    @GET("analytics/rendimiento-por-grado")
    suspend fun getRendimientoPorGrado(): Response<Map<String, Float>>

    @GET("analytics/eventos-proximos")
    suspend fun getEventosProximos(): Response<List<EventoInminente>>
}

// Modelo para eventos usando nombres exactos del cubo
data class EventoInminente(
    val titulo: String,
    val descripcion: String?,
    val fecha: String
)
package com.example.eduoptimaolapii.data.remote.api.olap

import com.example.eduoptimaolapii.data.model.olap.DimCalificacion
import com.example.eduoptimaolapii.data.model.olap.DimEstudiante
import com.example.eduoptimaolapii.data.model.olap.DimTiempo
import com.example.eduoptimaolapii.data.model.olap.FactNota
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OLAPQueryService {

    // CONSULTAS DIMENSIONALES
    @GET("olap/dim-estudiante")
    suspend fun getDimEstudiante(): Response<List<DimEstudiante>>

    @GET("olap/dim-tiempo")
    suspend fun getDimTiempo(): Response<List<DimTiempo>>

    @GET("olap/dim-calificacion")
    suspend fun getDimCalificacion(): Response<List<DimCalificacion>>

    // CONSULTAS DE HECHOS
    @GET("olap/fact-nota")
    suspend fun getFactNota(): Response<List<FactNota>>

    // CONSULTAS MULTIDIMENSIONALES
    @GET("olap/query/mdx")
    suspend fun executeMDXQuery(
        @Query("query") query: String
    ): Response<Map<String, Any>>

    @GET("olap/analytics/rendimiento-completo")
    suspend fun getRendimientoCompleto(): Response<Map<String, Any>>

    @GET("olap/analytics/tendencias-temporales")
    suspend fun getTendenciasTemporales(): Response<Map<String, Float>>

    @GET("olap/analytics/comparativa-grados")
    suspend fun getComparativaGrados(): Response<Map<String, Float>>

    @GET("olap/analytics/distribucion-calificaciones")
    suspend fun getDistribucionCalificaciones(): Response<Map<String, Int>>

    // CONSULTAS AVANZADAS
    @GET("olap/advanced/cubo-completo")
    suspend fun getCuboCompleto(): Response<Map<String, Any>>

    @GET("olap/advanced/drill-down")
    suspend fun getDrillDown(
        @Query("dimension") dimension: String,
        @Query("nivel") nivel: String
    ): Response<Map<String, Any>>

    @GET("olap/advanced/slice-dice")
    suspend fun getSliceDice(
        @Query("filtros") filtros: String
    ): Response<Map<String, Any>>

    // MÉTRICAS DEL CUBO
    @GET("olap/metrics/estadisticas-cubo")
    suspend fun getEstadisticasCubo(): Response<Map<String, Any>>

    @GET("olap/metrics/performance")
    suspend fun getPerformanceMetrics(): Response<Map<String, Long>>
}
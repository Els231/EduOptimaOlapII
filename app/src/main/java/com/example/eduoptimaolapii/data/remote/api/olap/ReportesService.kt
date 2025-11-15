package com.example.eduoptimaolapii.data.remote.api.olap

import com.example.eduoptimaolapii.data.model.olap.AlertaRendimiento
import com.example.eduoptimaolapii.data.model.olap.ChartData
import com.example.eduoptimaolapii.data.model.olap.ReporteAnalitico
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportesService {
    // CONSULTAS PARA REPORTES ANALÍTICOS DEL CUBO
    @GET("reportes/analitico")
    suspend fun getReporteAnalitico(): Response<ReporteAnalitico>

    @GET("analytics/notas-por-estudiante")
    suspend fun getNotasPorEstudiante(@Query("estudianteId") estudianteId: String): Response<List<ChartData>>

    @GET("analytics/alertas-rendimiento")
    suspend fun getAlertasRendimiento(): Response<List<AlertaRendimiento>>

    @GET("analytics/distribucion-sexo")
    suspend fun getDistribucionPorSexo(): Response<Map<String, Float>>

    @GET("analytics/rendimiento-trimestre")
    suspend fun getRendimientoPorTrimestre(): Response<Map<String, Float>>
}
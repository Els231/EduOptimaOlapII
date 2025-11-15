// File: app/src/main/java/com/example/eduoptimaolapii/data/repository/DashboardRepository.kt
package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.olap.DashboardResumen
import com.example.eduoptimaolapii.data.remote.api.olap.DashboardService
import com.example.eduoptimaolapii.data.remote.api.olap.EventoInminente
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val dashboardService: DashboardService
) {
    suspend fun getDashboardResumen(): DashboardResumen {
        val response = dashboardService.getDashboardResumen()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos")
        } else {
            throw Exception("Error API OLAP: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getMatriculasPorMes(): Map<String, Float> {
        val response = dashboardService.getMatriculasPorMes()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de matrículas")
        } else {
            throw Exception("Error al cargar matrículas: ${response.code()}")
        }
    }

    suspend fun getEstudiantesPorMunicipio(): Map<String, Float> {
        val response = dashboardService.getEstudiantesPorMunicipio()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución")
        } else {
            throw Exception("Error al cargar distribución: ${response.code()}")
        }
    }

    suspend fun getRendimientoPorGrado(): Map<String, Float> {
        val response = dashboardService.getRendimientoPorGrado()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de rendimiento")
        } else {
            throw Exception("Error al cargar rendimiento: ${response.code()}")
        }
    }

    suspend fun getEventosProximos(): List<com.example.eduoptimaolapii.data.remote.api.olap.EventoInminente> {
        val response = dashboardService.getEventosProximos()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de eventos")
        } else {
            throw Exception("Error al cargar eventos: ${response.code()}")
        }
    }
}
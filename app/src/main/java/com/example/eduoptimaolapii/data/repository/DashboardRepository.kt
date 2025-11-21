// File: app/src/main/java/com/example/eduoptimaolapii/data/repository/DashboardRepository.kt
package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.olap.DashboardResumen
import com.example.eduoptimaolapii.data.remote.api.olap.DashboardService
import com.example.eduoptimaolapii.data.remote.api.olap.OLAPQueryService
import com.example.eduoptimaolapii.data.remote.api.olap.EventoInminente
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val dashboardService: DashboardService,
    private val olapQueryService: OLAPQueryService
) {

    suspend fun getDashboardResumen(): DashboardResumen {
        return calculateDashboardFromOLAPData()
    }

    private suspend fun calculateDashboardFromOLAPData(): DashboardResumen {
        val factNotas = getAllNotasExtenso()
        val estudiantes = getEstudiantesCompletos()
        val calificaciones = getCalificacionesCompletas()

        // Calcular totalGrados de forma segura
        val totalGrados = try {
            calificaciones
                .mapNotNull { it.DescripcionCalificacion }
                .distinct()
                .size
        } catch (e: Exception) {
            null
        }

        return DashboardResumen(
            totalEstudiantes = estudiantes.distinctBy { it.id }.size,
            totalMatriculas = estudiantes.size,
            totalProfesores = 0,
            promedioGeneral = if (factNotas.isNotEmpty()) factNotas.map { it.valorNota }.average().toFloat() else 0f,
            tasaAprobacion = calculateTasaAprobacion(factNotas),
            totalGrados = totalGrados,
            ingresosTotales = 0.0,
            eventosProximos = 0,
            alertasRendimiento = calculateAlertasRendimiento(factNotas),
            ultimaActualizacion = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date())
        )
    }
    private fun calculateTasaAprobacion(factNotas: List<com.example.eduoptimaolapii.data.model.olap.FactNota>): Float {
        val totalNotas = factNotas.size
        if (totalNotas == 0) return 0f
        val aprobadas = factNotas.count { it.valorNota >= 3.0f }
        return (aprobadas.toFloat() / totalNotas) * 100
    }

    private fun calculateAlertasRendimiento(factNotas: List<com.example.eduoptimaolapii.data.model.olap.FactNota>): Int {
        val promediosEstudiantes = factNotas
            .groupBy { it.dimEstudiante?.id }
            .mapValues { (_, notas) -> notas.map { it.valorNota }.average() }

        return promediosEstudiantes.count { it.value < 3.0 }
    }

    suspend fun getPromedioPorTrimestre(): Map<String, Float> {
        val response = olapQueryService.getPromedioPorTrimestre()
        if (response.isSuccessful) {
            return response.body() ?: emptyMap()
        } else {
            throw Exception("Error al cargar promedios por trimestre: ${response.code()}")
        }
    }

    suspend fun getPromedioPorMunicipio(): Map<String, Float> {
        val response = olapQueryService.getPromedioPorMunicipio()
        if (response.isSuccessful) {
            return response.body() ?: emptyMap()
        } else {
            throw Exception("Error al cargar promedios por municipio: ${response.code()}")
        }
    }

    suspend fun getPromedioPorGrado(): Map<String, Float> {
        val response = olapQueryService.getPromedioPorGrado()
        if (response.isSuccessful) {
            return response.body() ?: emptyMap()
        } else {
            throw Exception("Error al cargar promedios por grado: ${response.code()}")
        }
    }

    suspend fun getEventosProximos(): List<EventoInminente> {
        return emptyList()
    }

    suspend fun getAllNotasExtenso(): List<com.example.eduoptimaolapii.data.model.olap.FactNota> {
        val response = olapQueryService.getAllFactNotas()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al cargar notas extenso: ${response.code()}")
        }
    }

    suspend fun getEstudiantesCompletos(): List<com.example.eduoptimaolapii.data.model.olap.DimEstudiante> {
        val response = olapQueryService.getAllEstudiantes()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al cargar estudiantes: ${response.code()}")
        }
    }

    suspend fun getCalificacionesCompletas(): List<com.example.eduoptimaolapii.data.model.olap.DimCalificacion> {
        val response = olapQueryService.getAllCalificaciones()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al cargar calificaciones: ${response.code()}")
        }
    }

    suspend fun getTiemposCompletos(): List<com.example.eduoptimaolapii.data.model.olap.DimTiempo> {
        val response = olapQueryService.getAllTiempos()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al cargar tiempos: ${response.code()}")
        }
    }
}
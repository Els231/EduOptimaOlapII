package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.mongodb.NotaMongo
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoNotaService
import javax.inject.Inject

class NotaRepository @Inject constructor(
    private val notaService: MongoNotaService
) {

    suspend fun getNotas(): List<NotaMongo> {
        val response = notaService.getNotas()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de notas")
        } else {
            throw Exception("Error API MongoDB: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getNotasPorEstudiante(estudianteId: String): List<NotaMongo> {
        val response = notaService.getNotasPorEstudiante(estudianteId)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de notas del estudiante")
        } else {
            throw Exception("Error al cargar notas del estudiante: ${response.code()}")
        }
    }

    suspend fun getNotasPorMateria(): Map<String, Float> {
        val response = notaService.getNotasPorMateria()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por materia")
        } else {
            throw Exception("Error al cargar distribución por materia: ${response.code()}")
        }
    }

    suspend fun getNotasPorPeriodo(): Map<String, Float> {
        val response = notaService.getNotasPorPeriodo()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por periodo")
        } else {
            throw Exception("Error al cargar distribución por periodo: ${response.code()}")
        }
    }

    suspend fun getPromediosGenerales(): Map<String, Float> {
        val response = notaService.getPromediosGenerales()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de promedios")
        } else {
            throw Exception("Error al cargar promedios: ${response.code()}")
        }
    }

    suspend fun getEstadisticasNotas(): Map<String, Any> {
        val response = notaService.getEstadisticasNotas()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de estadísticas")
        } else {
            throw Exception("Error al cargar estadísticas: ${response.code()}")
        }
    }

    suspend fun getAlertasRendimiento(): List<NotaMongo> {
        val response = notaService.getAlertasRendimiento()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al cargar alertas: ${response.code()}")
        }
    }
}
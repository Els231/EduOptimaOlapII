package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.mongodb.MatriculaMongo
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoMatriculaService
import javax.inject.Inject

class MatriculaRepository @Inject constructor(
    private val matriculaService: MongoMatriculaService
) {

    suspend fun getMatriculas(): List<MatriculaMongo> {
        val response = matriculaService.getMatriculas()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de matrículas")
        } else {
            throw Exception("Error API MongoDB: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getMatriculasActivas(): List<MatriculaMongo> {
        val response = matriculaService.getMatriculasActivas()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de matrículas activas")
        } else {
            throw Exception("Error al cargar matrículas activas: ${response.code()}")
        }
    }

    suspend fun getMatriculasPorGrado(): Map<String, Int> {
        val response = matriculaService.getMatriculasPorGrado()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por grado")
        } else {
            throw Exception("Error al cargar distribución por grado: ${response.code()}")
        }
    }

    suspend fun getMatriculasPorTurno(): Map<String, Int> {
        val response = matriculaService.getMatriculasPorTurno()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por turno")
        } else {
            throw Exception("Error al cargar distribución por turno: ${response.code()}")
        }
    }

    suspend fun getMatriculasPorAnio(): Map<String, Int> {
        val response = matriculaService.getMatriculasPorAnio()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por año")
        } else {
            throw Exception("Error al cargar distribución por año: ${response.code()}")
        }
    }

    suspend fun getEstadisticasMatriculas(): Map<String, Any> {
        val response = matriculaService.getEstadisticasMatriculas()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de estadísticas")
        } else {
            throw Exception("Error al cargar estadísticas: ${response.code()}")
        }
    }

    suspend fun buscarMatriculas(query: String): List<MatriculaMongo> {
        val response = matriculaService.buscarMatriculas(query)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error en búsqueda: ${response.code()}")
        }
    }
}
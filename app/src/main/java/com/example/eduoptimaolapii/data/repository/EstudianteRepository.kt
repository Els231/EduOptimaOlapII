package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo
import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEstudianteService
import javax.inject.Inject

class EstudianteRepository @Inject constructor(
    private val estudianteService: MongoEstudianteService
) {

    suspend fun getEstudiantes(): List<EstudianteMongo> {
        val response = estudianteService.getEstudiantes()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de estudiantes")
        } else {
            throw Exception("Error API MongoDB: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getEstudiantesActivos(): List<EstudianteMongo> {
        val response = estudianteService.getEstudiantesActivos()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de estudiantes activos")
        } else {
            throw Exception("Error al cargar estudiantes activos: ${response.code()}")
        }
    }

    suspend fun getEstudiantesPorMunicipio(): Map<String, Int> {
        val response = estudianteService.getEstudiantesPorMunicipio()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por municipio")
        } else {
            throw Exception("Error al cargar distribución por municipio: ${response.code()}")
        }
    }

    suspend fun getEstudiantesPorSexo(): Map<String, Int> {
        val response = estudianteService.getEstudiantesPorSexo()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por sexo")
        } else {
            throw Exception("Error al cargar distribución por sexo: ${response.code()}")
        }
    }

    suspend fun getEstudiantesPorGrado(): Map<String, Int> {
        val response = estudianteService.getEstudiantesPorGrado()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de distribución por grado")
        } else {
            throw Exception("Error al cargar distribución por grado: ${response.code()}")
        }
    }

    suspend fun buscarEstudiantes(query: String): List<EstudianteMongo> {
        val response = estudianteService.buscarEstudiantes(query)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error en búsqueda: ${response.code()}")
        }
    }

    suspend fun getEstadisticasEstudiantes(): Map<String, Any> {
        val response = estudianteService.getEstadisticasEstudiantes()
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("API retornó datos vacíos de estadísticas")
        } else {
            throw Exception("Error al cargar estadísticas: ${response.code()}")
        }
    }
}
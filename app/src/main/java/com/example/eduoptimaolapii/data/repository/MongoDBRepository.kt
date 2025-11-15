package com.example.eduoptimaolapii.data.repository

import com.example.eduoptimaolapii.data.remote.api.mongodb.MongoEventoService
import javax.inject.Inject

class MongoDBRepository @Inject constructor(
    private val mongoEventoService: MongoEventoService
) {
    suspend fun getCollections(): List<com.example.eduoptimaolapii.ui.viewmodels.MongoCollection> {
        return try {
            // Simulando datos de MongoDB para visualización
            listOf(
                com.example.eduoptimaolapii.ui.viewmodels.MongoCollection(
                    name = "estudiantes",
                    count = 1256,
                    documents = listOf(
                        mapOf("nombre" to "Juan Pérez", "grado" to "5°", "municipio" to "Managua"),
                        mapOf("nombre" to "María López", "grado" to "3°", "municipio" to "León")
                    )
                ),
                com.example.eduoptimaolapii.ui.viewmodels.MongoCollection(
                    name = "matriculas",
                    count = 2489,
                    documents = listOf(
                        mapOf("estudianteId" to "123", "fecha" to "2024-01-15", "estado" to "activa"),
                        mapOf("estudianteId" to "124", "fecha" to "2024-01-16", "estado" to "activa")
                    )
                )
            )
        } catch (e: Exception) {
            throw Exception("Error conexión MongoDB: ${e.message}")
        }
    }
}
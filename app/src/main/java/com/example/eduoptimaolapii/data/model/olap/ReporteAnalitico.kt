// File: app/src/main/java/com/example/eduoptimaolapii/data/model/olap/ReporteAnalitico.kt
package com.example.eduoptimaolapii.data.model.olap

data class ReporteAnalitico(
    val totalRegistros: Int,
    val fechaGeneracion: String,
    val metricas: Map<String, Any>,
    val tendencias: List<Tendencia>
)

data class Tendencia(
    val periodo: String,
    val valor: Float,
    val tipo: String // "Ascendente", "Descendente", "Estable"
)
// File: app/src/main/java/com/example/eduoptimaolapii/data/model/olap/AlertaRendimiento.kt
package com.example.eduoptimaolapii.data.model.olap

data class AlertaRendimiento(
    val estudianteId: String,
    val estudianteNombre: String,
    val grado: String,
    val materia: String,
    val promedio: Float,
    val estado: String // "Bajo", "Medio", "Crítico"
)
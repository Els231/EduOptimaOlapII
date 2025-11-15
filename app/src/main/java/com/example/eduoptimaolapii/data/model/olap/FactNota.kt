package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep
data class FactNota(
    val IdHechos: String,
    val DimEstudianteId: String,
    val DimCalificacionId: String,
    val DimTiempoId: String,
    val ValorNota: Float,
    val Ponderacion: Float,
    val TipoEvaluacion: String,
    val Materia: String,
    val Profesor: String?
)
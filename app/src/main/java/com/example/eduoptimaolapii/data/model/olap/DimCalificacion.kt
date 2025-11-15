package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep
data class DimCalificacion(
    val Id: String,
    val DescripcionCalificacion: String,
    val Grado: String,
    val Profesor: String?,
    val Materia: String?,
    val RangoMinimo: Float,
    val RangoMaximo: Float,
    val LetraCalificacion: String
)
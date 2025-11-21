package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep
// FactNota.kt - CORREGIDO
data class FactNota(
    val idHechos: String,
    val dimEstudiante: DimEstudiante,
    val dimCalificacion: DimCalificacion,
    val dimTiempo: DimTiempo,
    val valorNota: Float
)

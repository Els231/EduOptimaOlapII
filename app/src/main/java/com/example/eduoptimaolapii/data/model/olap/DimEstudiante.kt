package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep

// DimEstudiante.kt - CORREGIDO
data class DimEstudiante(
    val id: String,
    val nombre: String,
    val apellido: String,
    val nombreTutor: String,
    val apellidoTutor: String,
    val municipio: String,
    val departamento: String,
    val sexo: String
)

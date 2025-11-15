package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep
data class DimEstudiante(
    val Id: String,
    val Nombre: String,
    val Apellido: String,
    val NombreTutor: String?,
    val ApellidoTutor: String?,
    val Municipio: String?,
    val Departamento: String?,
    val Sexo: String,
    val Edad: Int?,
    val Grado: String?,
    val Turno: String?
)


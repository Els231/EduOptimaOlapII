package com.example.eduoptimaolapii.data.model.olap

data class EstudiantesDTO(
    val IdE: String,
    val Nombre: String,
    val Apellido: String,
    val NombreTutor: String,
    val ApellidoTutor: String,
    val Municipio: String,
    val Departamento: String,
    val Sexo: String
)

// CalificacionDTO.kt

data class CalificacionDTO(
    val IdC: String,
    val DescripcionCalificacion: String,
    val Grado: String,
    val Profesor: String
)

// TiempoDTO.kt

data class TiempoDTO(
    val IdT: String,
    val Fecha: String,
    val Dia: Int,
    val Mes: Int,
    val Año: Int,
    val Trimestre: Int
)

// AnalisisDTO.kt

data class AnalisisDTO(
    val Clave: String,
    val PromedioNotas: Float,
    val CantidadNotas: Int
)
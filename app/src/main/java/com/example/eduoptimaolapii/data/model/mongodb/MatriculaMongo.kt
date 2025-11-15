package com.example.eduoptimaolapii.data.model.mongodb

import androidx.annotation.Keep

@Keep
data class MatriculaMongo(
    val _id: String? = null,
    val CodigoMatricula: String,
    val FechaMatricula: String,
    val EstudianteIdEstudiante: String,
    val OfertaAcademicaIdOfertaAcademica: String? = null,
    val Estado: String = "Activa",
    val Grado: String? = null,
    val Turno: String? = null,
    val AnioAcademico: String,
    val FechaCreacion: String? = null,
    val FechaActualizacion: String? = null
)

@Keep
data class OfertaAcademicaMongo(
    val _id: String? = null,
    val CodigoOferta: String,
    val FechaOferta: String,
    val AnioOferta: String,
    val DescripcionOferta: String,
    val TurnoIdTurno: String? = null,
    val GradoIdGrado: String? = null
)

@Keep
data class TurnoMongo(
    val _id: String? = null,
    val CodigoTurno: String,
    val NombreTurno: String,
    val HorarioTurno: String
)

@Keep
data class GradoMongo(
    val _id: String? = null,
    val CodigoGrado: String,
    val NombreGrado: String,
    val Nivel: String
)
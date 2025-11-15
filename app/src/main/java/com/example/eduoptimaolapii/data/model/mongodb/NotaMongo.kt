package com.example.eduoptimaolapii.data.model.mongodb

import androidx.annotation.Keep

@Keep
data class NotaMongo(
    val _id: String? = null,
    val FechaGrabacion: String,
    val ValorNota: Float,
    val CalificacionIdCalificacion: String? = null,
    val DetalleMatriculaIdDetalleMatricula: String? = null,
    val EstudianteId: String,
    val MateriaId: String,
    val ProfesorId: String? = null,
    val Periodo: String,
    val TipoEvaluacion: String,
    val Ponderacion: Float = 1.0f,
    val Estado: String = "Activa",
    val FechaCreacion: String? = null
)

@Keep
data class CalificacionMongo(
    val _id: String? = null,
    val CodigoCalificacion: String,
    val DescripcionCalificacion: String,
    val RangoMinimo: Float,
    val RangoMaximo: Float,
    val LetraCalificacion: String
)

@Keep
data class DetalleMatriculaMongo(
    val _id: String? = null,
    val MatriculaIdMatricula: String,
    val MateriaIdMateria: String,
    val ProfesorIdProfesor: String? = null,
    val Estado: String = "Activo"
)
package com.example.eduoptimaolapii.data.model.mongodb

import androidx.annotation.Keep

@Keep
data class ProfesorMongo(
    val _id: String? = null,
    val CodigoProfesor: String,
    val Nombres: String,
    val Apellidos: String,
    val Sexo: String,
    val Cedula: String,
    val Especialidad: String? = null,
    val Telefono: String? = null,
    val Email: String? = null,
    val Direccion: String? = null,
    val Estado: String = "Activo",
    val FechaContratacion: String? = null,
    val FechaCreacion: String? = null
)


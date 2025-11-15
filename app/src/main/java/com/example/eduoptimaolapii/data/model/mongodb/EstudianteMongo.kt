package com.example.eduoptimaolapii.data.model.mongodb

import androidx.annotation.Keep

@Keep
data class EstudianteMongo(
    val _id: String? = null,
    val CodigoEstudiante: String,
    val Nombres: String,
    val Apellidos: String,
    val FechaNacimiento: String,
    val Sexo: String,
    val Cedula: String? = null,
    val TutorIdTutores: String? = null,
    val MunicipioIdMunicipio: String? = null,
    val Direccion: String? = null,
    val Telefono: String? = null,
    val Email: String? = null,
    val Estado: String = "Activo",
    val FechaCreacion: String? = null,
    val FechaActualizacion: String? = null
)

@Keep
data class TutorMongo(
    val _id: String? = null,
    val Cedula: String,
    val Nombres: String,
    val Apellidos: String,
    val LugarTrabajo: String? = null,
    val Telefono: String,
    val Email: String? = null,
    val Salario: Double? = null,
    val OcupacionIdOcupacion: String? = null
)

@Keep
data class MunicipioMongo(
    val _id: String? = null,
    val CodigoMunicipio: String,
    val NombreMunicipio: String,
    val DepartamentoIdDepartamento: String? = null
)

@Keep
data class DepartamentoMongo(
    val _id: String? = null,
    val CodigoDepartamento: String,
    val NombreDepartamento: String
)
package com.example.eduoptimaolapii.data.model.mongodb

import androidx.annotation.Keep

@Keep
data class MateriaMongo(
    val _id: String? = null,
    val CodigoMateria: String,
    val NombreMateria: String,
    val Descripcion: String? = null,
    val Creditos: Int = 1,
    val HorasSemanales: Int = 4,
    val Area: String? = null,
    val Estado: String = "Activa"
)
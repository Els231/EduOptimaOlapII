// File: app/src/main/java/com/example/eduoptimaolapii/data/model/mongodb/UsuarioMongo.kt
package com.example.eduoptimaolapii.data.model.mongodb

data class UsuarioMongo(
    val _id: String? = null,
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String,
    val rol: String,
    val fechaCreacion: String? = null
)
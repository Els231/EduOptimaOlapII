// File: app/src/main/java/com/example/eduoptimaolapii/data/model/mongodb/EventoMongo.kt
package com.example.eduoptimaolapii.data.model.mongodb

data class EventoMongo(
    val _id: String? = null,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val tipo: String
)
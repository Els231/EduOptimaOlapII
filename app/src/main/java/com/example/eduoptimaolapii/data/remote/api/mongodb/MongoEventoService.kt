// File: app/src/main/java/com/example/eduoptimaolapii/data/remote/api/mongodb/MongoEventoService.kt
package com.example.eduoptimaolapii.data.remote.api.mongodb

import com.example.eduoptimaolapii.data.model.mongodb.EventoMongo
import retrofit2.Response
import retrofit2.http.GET

interface MongoEventoService {
    // CONSULTAS SOLO LECTURA PARA VISUALIZAR DATOS DE MONGODB
    @GET("eventos")
    suspend fun getEventos(): Response<List<EventoMongo>>

    @GET("eventos/proximos")
    suspend fun getEventosProximos(): Response<List<EventoMongo>>

    @GET("estudiantes")
    suspend fun getEstudiantes(): Response<List<Any>>

    @GET("matriculas")
    suspend fun getMatriculas(): Response<List<Any>>

    @GET("profesores")
    suspend fun getProfesores(): Response<List<Any>>
}
package com.example.eduoptimaolapii.data.model.olap

data class DashboardResumen(
    val totalEstudiantes: Int,
    val totalMatriculas: Int,
    val totalProfesores: Int,
    val promedioGeneral: Float,
    val tasaAprobacion: Float,
    val ingresosTotales: Double,
    val eventosProximos: Int,
    val alertasRendimiento: Int,
    val ultimaActualizacion: String
)
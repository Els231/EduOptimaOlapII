package com.example.eduoptimaolapii.data.model.olap

import androidx.annotation.Keep

@Keep
data class DimTiempo(
    val IdDimTiempo: String,
    val Fecha: String,
    val Dia: Int,
    val Mes: Int,
    val Ano: Int,
    val Trimestre: Int,
    val Semestre: Int,
    val DiaSemana: String,
    val MesNombre: String,
    val EsFinDeSemana: Boolean,
    val EsFeriado: Boolean
)


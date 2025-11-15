package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.olap.DimCalificacion
import com.example.eduoptimaolapii.data.model.olap.DimEstudiante
import com.example.eduoptimaolapii.data.model.olap.DimTiempo
import com.example.eduoptimaolapii.data.model.olap.FactNota
import com.example.eduoptimaolapii.data.remote.api.olap.OLAPQueryService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OLAPState(
    val isLoading: Boolean = false,
    val dimEstudiante: List<DimEstudiante> = emptyList(),
    val dimTiempo: List<DimTiempo> = emptyList(),
    val dimCalificacion: List<DimCalificacion> = emptyList(),
    val factNota: List<FactNota> = emptyList(),
    val rendimientoCompleto: Map<String, Any> = emptyMap(),
    val tendenciasTemporales: Map<String, Float> = emptyMap(),
    val comparativaGrados: Map<String, Float> = emptyMap(),
    val distribucionCalificaciones: Map<String, Int> = emptyMap(),
    val queryResults: Map<String, Any> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class OLAPViewModel @Inject constructor(
    private val olapQueryService: OLAPQueryService
) : ViewModel() {

    private val _olapState = MutableStateFlow(OLAPState())
    val olapState: StateFlow<OLAPState> = _olapState.asStateFlow()

    init {
        loadOLAPData()
    }

    fun loadOLAPData() {
        _olapState.value = _olapState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val dimEstudiante = olapQueryService.getDimEstudiante()
                val dimTiempo = olapQueryService.getDimTiempo()
                val dimCalificacion = olapQueryService.getDimCalificacion()
                val factNota = olapQueryService.getFactNota()
                val rendimiento = olapQueryService.getRendimientoCompleto()
                val tendencias = olapQueryService.getTendenciasTemporales()
                val comparativa = olapQueryService.getComparativaGrados()
                val distribucion = olapQueryService.getDistribucionCalificaciones()

                _olapState.value = OLAPState(
                    isLoading = false,
                    dimEstudiante = dimEstudiante.body() ?: emptyList(),
                    dimTiempo = dimTiempo.body() ?: emptyList(),
                    dimCalificacion = dimCalificacion.body() ?: emptyList(),
                    factNota = factNota.body() ?: emptyList(),
                    rendimientoCompleto = rendimiento.body() ?: emptyMap(),
                    tendenciasTemporales = tendencias.body() ?: emptyMap(),
                    comparativaGrados = comparativa.body() ?: emptyMap(),
                    distribucionCalificaciones = distribucion.body() ?: emptyMap()
                )
            } catch (e: Exception) {
                _olapState.value = _olapState.value.copy(
                    isLoading = false,
                    error = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "❌ Error de conexión OLAP"
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "⏰ Timeout cubo OLAP"
                        e.message?.contains("404", ignoreCase = true) == true ->
                            "🔍 Cubo OLAP no disponible"
                        else -> "❌ Error OLAP: ${e.message}"
                    }
                )
            }
        }
    }

    fun executeMDXQuery(query: String) {
        _olapState.value = _olapState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val response = olapQueryService.executeMDXQuery(query)
                _olapState.value = _olapState.value.copy(
                    isLoading = false,
                    queryResults = response.body() ?: emptyMap()
                )
            } catch (e: Exception) {
                _olapState.value = _olapState.value.copy(
                    isLoading = false,
                    error = "Error en consulta MDX: ${e.message}"
                )
            }
        }
    }

    fun getDrillDown(dimension: String, nivel: String) {
        viewModelScope.launch {
            try {
                val response = olapQueryService.getDrillDown(dimension, nivel)
                _olapState.value = _olapState.value.copy(
                    queryResults = response.body() ?: emptyMap()
                )
            } catch (e: Exception) {
                _olapState.value = _olapState.value.copy(
                    error = "Error drill-down: ${e.message}"
                )
            }
        }
    }

    fun refreshData() {
        loadOLAPData()
    }

    fun clearError() {
        _olapState.value = _olapState.value.copy(error = null)
    }
}
package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.mongodb.NotaMongo
import com.example.eduoptimaolapii.data.repository.NotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotaState(
    val isLoading: Boolean = false,
    val notas: List<NotaMongo> = emptyList(),
    val notasFiltradas: List<NotaMongo> = emptyList(),
    val notasPorMateria: Map<String, Float> = emptyMap(),
    val notasPorPeriodo: Map<String, Float> = emptyMap(),
    val promediosGenerales: Map<String, Float> = emptyMap(),
    val estadisticas: Map<String, Any> = emptyMap(),
    val alertasRendimiento: List<NotaMongo> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class NotaViewModel @Inject constructor(
    private val notaRepository: NotaRepository
) : ViewModel() {

    private val _notaState = MutableStateFlow(NotaState())
    val notaState: StateFlow<NotaState> = _notaState.asStateFlow()

    init {
        loadNotasData()
    }

    fun loadNotasData() {
        _notaState.value = _notaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val notas = notaRepository.getNotas()
                val porMateria = notaRepository.getNotasPorMateria()
                val porPeriodo = notaRepository.getNotasPorPeriodo()
                val promedios = notaRepository.getPromediosGenerales()
                val estadisticas = notaRepository.getEstadisticasNotas()
                val alertas = notaRepository.getAlertasRendimiento()

                _notaState.value = NotaState(
                    isLoading = false,
                    notas = notas,
                    notasFiltradas = notas,
                    notasPorMateria = porMateria,
                    notasPorPeriodo = porPeriodo,
                    promediosGenerales = promedios,
                    estadisticas = estadisticas,
                    alertasRendimiento = alertas
                )
            } catch (e: Exception) {
                _notaState.value = _notaState.value.copy(
                    isLoading = false,
                    error = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "❌ Error de conexión. Verifique su internet"
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "⏰ Timeout. Las APIs no responden"
                        e.message?.contains("404", ignoreCase = true) == true ->
                            "🔍 API no encontrada. Verifique las URLs"
                        e.message?.contains("401", ignoreCase = true) == true ->
                            "🔐 No autorizado. Verifique credenciales"
                        e.message?.contains("500", ignoreCase = true) == true ->
                            "⚡ Error del servidor. Intente más tarde"
                        else -> "❌ Error al cargar notas: ${e.message}"
                    }
                )
            }
        }
    }

    fun loadNotasPorEstudiante(estudianteId: String) {
        _notaState.value = _notaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val notasEstudiante = notaRepository.getNotasPorEstudiante(estudianteId)
                _notaState.value = _notaState.value.copy(
                    isLoading = false,
                    notas = notasEstudiante,
                    notasFiltradas = notasEstudiante
                )
            } catch (e: Exception) {
                _notaState.value = _notaState.value.copy(
                    isLoading = false,
                    error = "Error al cargar notas del estudiante: ${e.message}"
                )
            }
        }
    }

    fun searchNotas(query: String) {
        _notaState.value = _notaState.value.copy(searchQuery = query)

        if (query.isEmpty()) {
            _notaState.value = _notaState.value.copy(
                notasFiltradas = _notaState.value.notas
            )
        } else {
            val filtered = _notaState.value.notas.filter { nota ->
                nota.TipoEvaluacion.contains(query, ignoreCase = true) ||
                        nota.Periodo.contains(query, ignoreCase = true) ||
                        nota.Estado.contains(query, ignoreCase = true)
            }
            _notaState.value = _notaState.value.copy(
                notasFiltradas = filtered
            )
        }
    }

    fun refreshData() {
        loadNotasData()
    }

    fun clearError() {
        _notaState.value = _notaState.value.copy(error = null)
    }
}
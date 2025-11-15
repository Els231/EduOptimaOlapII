package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.mongodb.MatriculaMongo
import com.example.eduoptimaolapii.data.repository.MatriculaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatriculaState(
    val isLoading: Boolean = false,
    val matriculas: List<MatriculaMongo> = emptyList(),
    val matriculasFiltradas: List<MatriculaMongo> = emptyList(),
    val matriculasPorGrado: Map<String, Int> = emptyMap(),
    val matriculasPorTurno: Map<String, Int> = emptyMap(),
    val matriculasPorAnio: Map<String, Int> = emptyMap(),
    val estadisticas: Map<String, Any> = emptyMap(),
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class MatriculaViewModel @Inject constructor(
    private val matriculaRepository: MatriculaRepository
) : ViewModel() {

    private val _matriculaState = MutableStateFlow(MatriculaState())
    val matriculaState: StateFlow<MatriculaState> = _matriculaState.asStateFlow()

    init {
        loadMatriculasData()
    }

    fun loadMatriculasData() {
        _matriculaState.value = _matriculaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val matriculas = matriculaRepository.getMatriculas()
                val porGrado = matriculaRepository.getMatriculasPorGrado()
                val porTurno = matriculaRepository.getMatriculasPorTurno()
                val porAnio = matriculaRepository.getMatriculasPorAnio()
                val estadisticas = matriculaRepository.getEstadisticasMatriculas()

                _matriculaState.value = MatriculaState(
                    isLoading = false,
                    matriculas = matriculas,
                    matriculasFiltradas = matriculas,
                    matriculasPorGrado = porGrado,
                    matriculasPorTurno = porTurno,
                    matriculasPorAnio = porAnio,
                    estadisticas = estadisticas
                )
            } catch (e: Exception) {
                _matriculaState.value = _matriculaState.value.copy(
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
                        else -> "❌ Error al cargar matrículas: ${e.message}"
                    }
                )
            }
        }
    }

    fun searchMatriculas(query: String) {
        _matriculaState.value = _matriculaState.value.copy(searchQuery = query)

        if (query.isEmpty()) {
            _matriculaState.value = _matriculaState.value.copy(
                matriculasFiltradas = _matriculaState.value.matriculas
            )
        } else {
            viewModelScope.launch {
                try {
                    val filtered = matriculaRepository.buscarMatriculas(query)
                    _matriculaState.value = _matriculaState.value.copy(
                        matriculasFiltradas = filtered
                    )
                } catch (e: Exception) {
                    // Si falla la búsqueda en API, filtrar localmente
                    val filtered = _matriculaState.value.matriculas.filter { matricula ->
                        matricula.CodigoMatricula.contains(query, ignoreCase = true) ||
                                matricula.Estado.contains(query, ignoreCase = true) ||
                                matricula.Grado?.contains(query, ignoreCase = true) == true
                    }
                    _matriculaState.value = _matriculaState.value.copy(
                        matriculasFiltradas = filtered
                    )
                }
            }
        }
    }

    fun refreshData() {
        loadMatriculasData()
    }

    fun clearError() {
        _matriculaState.value = _matriculaState.value.copy(error = null)
    }
}
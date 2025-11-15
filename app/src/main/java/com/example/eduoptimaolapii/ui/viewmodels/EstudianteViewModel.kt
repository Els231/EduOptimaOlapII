package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo
import com.example.eduoptimaolapii.data.repository.EstudianteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EstudianteState(
    val isLoading: Boolean = false,
    val estudiantes: List<EstudianteMongo> = emptyList(),
    val estudiantesFiltrados: List<EstudianteMongo> = emptyList(),
    val estudiantesPorMunicipio: Map<String, Int> = emptyMap(),
    val estudiantesPorSexo: Map<String, Int> = emptyMap(),
    val estudiantesPorGrado: Map<String, Int> = emptyMap(),
    val estadisticas: Map<String, Any> = emptyMap(),
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class EstudianteViewModel @Inject constructor(
    private val estudianteRepository: EstudianteRepository
) : ViewModel() {

    private val _estudianteState = MutableStateFlow(EstudianteState())
    val estudianteState: StateFlow<EstudianteState> = _estudianteState.asStateFlow()

    init {
        loadEstudiantesData()
    }

    fun loadEstudiantesData() {
        _estudianteState.value = _estudianteState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val estudiantes = estudianteRepository.getEstudiantes()
                val porMunicipio = estudianteRepository.getEstudiantesPorMunicipio()
                val porSexo = estudianteRepository.getEstudiantesPorSexo()
                val porGrado = estudianteRepository.getEstudiantesPorGrado()
                val estadisticas = estudianteRepository.getEstadisticasEstudiantes()

                _estudianteState.value = EstudianteState(
                    isLoading = false,
                    estudiantes = estudiantes,
                    estudiantesFiltrados = estudiantes,
                    estudiantesPorMunicipio = porMunicipio,
                    estudiantesPorSexo = porSexo,
                    estudiantesPorGrado = porGrado,
                    estadisticas = estadisticas
                )
            } catch (e: Exception) {
                _estudianteState.value = _estudianteState.value.copy(
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
                        else -> "❌ Error al cargar estudiantes: ${e.message}"
                    }
                )
            }
        }
    }

    fun searchEstudiantes(query: String) {
        _estudianteState.value = _estudianteState.value.copy(searchQuery = query)

        if (query.isEmpty()) {
            _estudianteState.value = _estudianteState.value.copy(
                estudiantesFiltrados = _estudianteState.value.estudiantes
            )
        } else {
            val filtered = _estudianteState.value.estudiantes.filter { estudiante ->
                estudiante.Nombres.contains(query, ignoreCase = true) ||
                        estudiante.Apellidos.contains(query, ignoreCase = true) ||
                        estudiante.CodigoEstudiante.contains(query, ignoreCase = true)
            }
            _estudianteState.value = _estudianteState.value.copy(
                estudiantesFiltrados = filtered
            )
        }
    }

    fun refreshData() {
        loadEstudiantesData()
    }

    fun clearError() {
        _estudianteState.value = _estudianteState.value.copy(error = null)
    }
}
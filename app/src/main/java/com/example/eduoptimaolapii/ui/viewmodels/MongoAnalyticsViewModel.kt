package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.repository.EstudianteRepository
import com.example.eduoptimaolapii.data.repository.MatriculaRepository
import com.example.eduoptimaolapii.data.repository.NotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MongoAnalyticsState(
    val isLoading: Boolean = false,
    val totalEstudiantes: Int = 0,
    val totalMatriculas: Int = 0,
    val totalNotas: Int = 0,
    val estadisticasCompletas: Map<String, Any> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class MongoAnalyticsViewModel @Inject constructor(
    private val estudianteRepository: EstudianteRepository,
    private val matriculaRepository: MatriculaRepository,
    private val notaRepository: NotaRepository
) : ViewModel() {

    private val _analyticsState = MutableStateFlow(MongoAnalyticsState())
    val analyticsState: StateFlow<MongoAnalyticsState> = _analyticsState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    fun loadAnalyticsData() {
        _analyticsState.value = _analyticsState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val estudiantes = estudianteRepository.getEstudiantes()
                val matriculas = matriculaRepository.getMatriculas()
                val notas = notaRepository.getNotas()
                val statsEstudiantes = estudianteRepository.getEstadisticasEstudiantes()
                val statsMatriculas = matriculaRepository.getEstadisticasMatriculas()
                val statsNotas = notaRepository.getEstadisticasNotas()

                _analyticsState.value = MongoAnalyticsState(
                    isLoading = false,
                    totalEstudiantes = estudiantes.size,
                    totalMatriculas = matriculas.size,
                    totalNotas = notas.size,
                    estadisticasCompletas = mapOf(
                        "estudiantes" to statsEstudiantes,
                        "matriculas" to statsMatriculas,
                        "notas" to statsNotas
                    )
                )
            } catch (e: Exception) {
                _analyticsState.value = _analyticsState.value.copy(
                    isLoading = false,
                    error = "Error analytics: ${e.message}"
                )
            }
        }
    }

    fun refreshData() {
        loadAnalyticsData()
    }
}
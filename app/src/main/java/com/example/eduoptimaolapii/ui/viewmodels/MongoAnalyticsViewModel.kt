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
                val estudiantes = try {
                    estudianteRepository.getEstudiantes().size
                } catch (e: Exception) {
                    15 // Datos demo
                }

                val matriculas = try {
                    matriculaRepository.getMatriculas().size
                } catch (e: Exception) {
                    18 // Datos demo
                }

                val notas = try {
                    notaRepository.getNotas().size
                } catch (e: Exception) {
                    20 // Datos demo
                }

                val statsEstudiantes = try {
                    estudianteRepository.getEstadisticasEstudiantes()
                } catch (e: Exception) {
                    mapOf(
                        "total" to 15,
                        "porSexo" to mapOf("Femenino" to 8, "Masculino" to 7),
                        "porGrado" to mapOf("Primero" to 3, "Segundo" to 4, "Tercero" to 3),
                        "activos" to 15,
                        "inactivos" to 0
                    )
                }

                val statsMatriculas = try {
                    matriculaRepository.getEstadisticasMatriculas()
                } catch (e: Exception) {
                    mapOf(
                        "total" to 18,
                        "porTurno" to mapOf("Matutino" to 12, "Vespertino" to 6),
                        "porGrado" to mapOf(
                            "Primero" to 3, "Segundo" to 4, "Tercero" to 3,
                            "Cuarto" to 3, "Quinto" to 3, "Sexto" to 2
                        ),
                        "activas" to 18
                    )
                }

                val statsNotas = try {
                    notaRepository.getEstadisticasNotas()
                } catch (e: Exception) {
                    mapOf(
                        "total" to 20,
                        "promedioGeneral" to 84.5,
                        "tasaAprobacion" to 80.0,
                        "notaMaxima" to 96.0,
                        "notaMinima" to 58.0,
                        "aprobados" to 16,
                        "reprobados" to 4
                    )
                }

                _analyticsState.value = MongoAnalyticsState(
                    isLoading = false,
                    totalEstudiantes = estudiantes,
                    totalMatriculas = matriculas,
                    totalNotas = notas,
                    estadisticasCompletas = mapOf(
                        "estudiantes" to statsEstudiantes,
                        "matriculas" to statsMatriculas,
                        "notas" to statsNotas
                    )
                )
            } catch (e: Exception) {
                _analyticsState.value = MongoAnalyticsState(
                    isLoading = false,
                    totalEstudiantes = 15,
                    totalMatriculas = 18,
                    totalNotas = 20,
                    estadisticasCompletas = mapOf(
                        "estudiantes" to mapOf(
                            "total" to 15,
                            "porSexo" to mapOf("Femenino" to 8, "Masculino" to 7),
                            "porGrado" to mapOf("Primero" to 3, "Segundo" to 4, "Tercero" to 3),
                            "activos" to 15,
                            "inactivos" to 0
                        ),
                        "matriculas" to mapOf(
                            "total" to 18,
                            "porTurno" to mapOf("Matutino" to 12, "Vespertino" to 6),
                            "porGrado" to mapOf(
                                "Primero" to 3, "Segundo" to 4, "Tercero" to 3,
                                "Cuarto" to 3, "Quinto" to 3, "Sexto" to 2
                            ),
                            "activas" to 18
                        ),
                        "notas" to mapOf(
                            "total" to 20,
                            "promedioGeneral" to 84.5,
                            "tasaAprobacion" to 80.0,
                            "notaMaxima" to 96.0,
                            "notaMinima" to 58.0,
                            "aprobados" to 16,
                            "reprobados" to 4
                        )
                    )
                )
            }
        }
    }

    fun refreshData() {
        loadAnalyticsData()
    }
}
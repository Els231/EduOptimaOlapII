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

    // Datos estáticos para demostración - CORREGIDOS con la estructura correcta
    private val matriculasDemo = listOf(
        MatriculaMongo(
            _id = "1",
            CodigoMatricula = "MAT2024001",
            FechaMatricula = "2024-01-15",
            EstudianteIdEstudiante = "EST2024001",
            Estado = "Activa",
            Grado = "Primero",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "2",
            CodigoMatricula = "MAT2024002",
            FechaMatricula = "2024-01-15",
            EstudianteIdEstudiante = "EST2024002",
            Estado = "Activa",
            Grado = "Segundo",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "3",
            CodigoMatricula = "MAT2024003",
            FechaMatricula = "2024-01-16",
            EstudianteIdEstudiante = "EST2024003",
            Estado = "Activa",
            Grado = "Tercero",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "4",
            CodigoMatricula = "MAT2024004",
            FechaMatricula = "2024-01-16",
            EstudianteIdEstudiante = "EST2024004",
            Estado = "Activa",
            Grado = "Cuarto",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "5",
            CodigoMatricula = "MAT2024005",
            FechaMatricula = "2024-01-17",
            EstudianteIdEstudiante = "EST2024005",
            Estado = "Activa",
            Grado = "Quinto",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "6",
            CodigoMatricula = "MAT2024006",
            FechaMatricula = "2024-01-17",
            EstudianteIdEstudiante = "EST2024006",
            Estado = "Activa",
            Grado = "Sexto",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "7",
            CodigoMatricula = "MAT2024007",
            FechaMatricula = "2024-01-18",
            EstudianteIdEstudiante = "EST2024007",
            Estado = "Activa",
            Grado = "Primero",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "8",
            CodigoMatricula = "MAT2024008",
            FechaMatricula = "2024-01-18",
            EstudianteIdEstudiante = "EST2024008",
            Estado = "Activa",
            Grado = "Segundo",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "9",
            CodigoMatricula = "MAT2024009",
            FechaMatricula = "2024-01-19",
            EstudianteIdEstudiante = "EST2024009",
            Estado = "Activa",
            Grado = "Tercero",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "10",
            CodigoMatricula = "MAT2024010",
            FechaMatricula = "2024-01-19",
            EstudianteIdEstudiante = "EST2024010",
            Estado = "Activa",
            Grado = "Cuarto",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "11",
            CodigoMatricula = "MAT2024011",
            FechaMatricula = "2024-01-20",
            EstudianteIdEstudiante = "EST2024011",
            Estado = "Activa",
            Grado = "Quinto",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "12",
            CodigoMatricula = "MAT2024012",
            FechaMatricula = "2024-01-20",
            EstudianteIdEstudiante = "EST2024012",
            Estado = "Activa",
            Grado = "Sexto",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "13",
            CodigoMatricula = "MAT2024013",
            FechaMatricula = "2024-01-21",
            EstudianteIdEstudiante = "EST2024013",
            Estado = "Activa",
            Grado = "Primero",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "14",
            CodigoMatricula = "MAT2024014",
            FechaMatricula = "2024-01-21",
            EstudianteIdEstudiante = "EST2024014",
            Estado = "Activa",
            Grado = "Segundo",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "15",
            CodigoMatricula = "MAT2024015",
            FechaMatricula = "2024-01-22",
            EstudianteIdEstudiante = "EST2024015",
            Estado = "Activa",
            Grado = "Tercero",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "16",
            CodigoMatricula = "MAT2024016",
            FechaMatricula = "2024-01-22",
            EstudianteIdEstudiante = "EST2024001",
            Estado = "Activa",
            Grado = "Cuarto",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "17",
            CodigoMatricula = "MAT2024017",
            FechaMatricula = "2024-01-23",
            EstudianteIdEstudiante = "EST2024002",
            Estado = "Activa",
            Grado = "Quinto",
            Turno = "Matutino",
            AnioAcademico = "2024"
        ),
        MatriculaMongo(
            _id = "18",
            CodigoMatricula = "MAT2024018",
            FechaMatricula = "2024-01-23",
            EstudianteIdEstudiante = "EST2024003",
            Estado = "Activa",
            Grado = "Sexto",
            Turno = "Vespertino",
            AnioAcademico = "2024"
        )
    )

    init {
        loadMatriculasData()
    }

    fun loadMatriculasData() {
        _matriculaState.value = _matriculaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val matriculas = try {
                    matriculaRepository.getMatriculas()
                } catch (e: Exception) {
                    matriculasDemo
                }

                val porGrado = try {
                    matriculaRepository.getMatriculasPorGrado()
                } catch (e: Exception) {
                    mapOf(
                        "Primero" to 3, "Segundo" to 4, "Tercero" to 3,
                        "Cuarto" to 3, "Quinto" to 3, "Sexto" to 2
                    )
                }

                val porTurno = try {
                    matriculaRepository.getMatriculasPorTurno()
                } catch (e: Exception) {
                    mapOf("Matutino" to 12, "Vespertino" to 6)
                }

                val porAnio = try {
                    matriculaRepository.getMatriculasPorAnio()
                } catch (e: Exception) {
                    mapOf("2024" to 18)
                }

                val estadisticas = try {
                    matriculaRepository.getEstadisticasMatriculas()
                } catch (e: Exception) {
                    mapOf(
                        "total" to 18,
                        "activas" to 18,
                        "inactivas" to 0,
                        "tasaRetencion" to 95.0
                    )
                }

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
                    error = "Error cargando matrículas. Usando datos de demostración"
                )

                // Fallback completo
                _matriculaState.value = MatriculaState(
                    isLoading = false,
                    matriculas = matriculasDemo,
                    matriculasFiltradas = matriculasDemo,
                    matriculasPorGrado = mapOf(
                        "Primero" to 3, "Segundo" to 4, "Tercero" to 3,
                        "Cuarto" to 3, "Quinto" to 3, "Sexto" to 2
                    ),
                    matriculasPorTurno = mapOf("Matutino" to 12, "Vespertino" to 6),
                    matriculasPorAnio = mapOf("2024" to 18),
                    estadisticas = mapOf(
                        "total" to 18, "activas" to 18, "inactivas" to 0, "tasaRetencion" to 95.0
                    )
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
            val filtered = _matriculaState.value.matriculas.filter { matricula ->
                matricula.CodigoMatricula.contains(query, ignoreCase = true) ||
                        matricula.Estado.contains(query, ignoreCase = true) ||
                        matricula.Grado?.contains(query, ignoreCase = true) == true ||
                        matricula.Turno?.contains(query, ignoreCase = true) == true
            }
            _matriculaState.value = _matriculaState.value.copy(
                matriculasFiltradas = filtered
            )
        }
    }

    fun refreshData() {
        loadMatriculasData()
    }

    fun clearError() {
        _matriculaState.value = _matriculaState.value.copy(error = null)
    }
}
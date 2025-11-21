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

    // Datos estáticos de demostración - CORREGIDOS con la estructura correcta
    private val notasDemo = listOf(
        NotaMongo(
            _id = "1",
            FechaGrabacion = "2024-03-15",
            ValorNota = 95.0f,
            EstudianteId = "EST2024001",
            MateriaId = "MAT001",
            Periodo = "2024-1",
            TipoEvaluacion = "Examen Parcial",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "2",
            FechaGrabacion = "2024-03-16",
            ValorNota = 88.0f,
            EstudianteId = "EST2024002",
            MateriaId = "CIE001",
            Periodo = "2024-1",
            TipoEvaluacion = "Tarea",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "3",
            FechaGrabacion = "2024-03-17",
            ValorNota = 72.0f,
            EstudianteId = "EST2024003",
            MateriaId = "HIS001",
            Periodo = "2024-1",
            TipoEvaluacion = "Examen Final",
            Ponderacion = 0.4f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "4",
            FechaGrabacion = "2024-03-18",
            ValorNota = 65.0f,
            EstudianteId = "EST2024004",
            MateriaId = "ING001",
            Periodo = "2024-1",
            TipoEvaluacion = "Quiz",
            Ponderacion = 0.1f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "5",
            FechaGrabacion = "2024-03-19",
            ValorNota = 92.0f,
            EstudianteId = "EST2024005",
            MateriaId = "MAT001",
            Periodo = "2024-1",
            TipoEvaluacion = "Proyecto",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "6",
            FechaGrabacion = "2024-03-20",
            ValorNota = 78.0f,
            EstudianteId = "EST2024006",
            MateriaId = "FIS001",
            Periodo = "2024-1",
            TipoEvaluacion = "Examen Parcial",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "7",
            FechaGrabacion = "2024-03-21",
            ValorNota = 85.0f,
            EstudianteId = "EST2024007",
            MateriaId = "QUI001",
            Periodo = "2024-1",
            TipoEvaluacion = "Laboratorio",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "8",
            FechaGrabacion = "2024-03-22",
            ValorNota = 91.0f,
            EstudianteId = "EST2024008",
            MateriaId = "BIO001",
            Periodo = "2024-1",
            TipoEvaluacion = "Examen Final",
            Ponderacion = 0.4f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "9",
            FechaGrabacion = "2024-03-23",
            ValorNota = 69.0f,
            EstudianteId = "EST2024009",
            MateriaId = "LIT001",
            Periodo = "2024-1",
            TipoEvaluacion = "Tarea",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "10",
            FechaGrabacion = "2024-03-24",
            ValorNota = 94.0f,
            EstudianteId = "EST2024010",
            MateriaId = "ART001",
            Periodo = "2024-1",
            TipoEvaluacion = "Proyecto",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "11",
            FechaGrabacion = "2024-06-15",
            ValorNota = 81.0f,
            EstudianteId = "EST2024001",
            MateriaId = "MAT001",
            Periodo = "2024-2",
            TipoEvaluacion = "Quiz",
            Ponderacion = 0.1f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "12",
            FechaGrabacion = "2024-06-16",
            ValorNota = 87.0f,
            EstudianteId = "EST2024002",
            MateriaId = "CIE001",
            Periodo = "2024-2",
            TipoEvaluacion = "Examen Parcial",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "13",
            FechaGrabacion = "2024-06-17",
            ValorNota = 73.0f,
            EstudianteId = "EST2024003",
            MateriaId = "HIS001",
            Periodo = "2024-2",
            TipoEvaluacion = "Tarea",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "14",
            FechaGrabacion = "2024-06-18",
            ValorNota = 58.0f,
            EstudianteId = "EST2024004",
            MateriaId = "ING001",
            Periodo = "2024-2",
            TipoEvaluacion = "Examen Final",
            Ponderacion = 0.4f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "15",
            FechaGrabacion = "2024-06-19",
            ValorNota = 96.0f,
            EstudianteId = "EST2024005",
            MateriaId = "FIS001",
            Periodo = "2024-2",
            TipoEvaluacion = "Proyecto",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "16",
            FechaGrabacion = "2024-06-20",
            ValorNota = 82.0f,
            EstudianteId = "EST2024006",
            MateriaId = "QUI001",
            Periodo = "2024-2",
            TipoEvaluacion = "Laboratorio",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "17",
            FechaGrabacion = "2024-06-21",
            ValorNota = 89.0f,
            EstudianteId = "EST2024007",
            MateriaId = "BIO001",
            Periodo = "2024-2",
            TipoEvaluacion = "Quiz",
            Ponderacion = 0.1f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "18",
            FechaGrabacion = "2024-06-22",
            ValorNota = 77.0f,
            EstudianteId = "EST2024008",
            MateriaId = "LIT001",
            Periodo = "2024-2",
            TipoEvaluacion = "Examen Parcial",
            Ponderacion = 0.3f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "19",
            FechaGrabacion = "2024-06-23",
            ValorNota = 63.0f,
            EstudianteId = "EST2024009",
            MateriaId = "ART001",
            Periodo = "2024-2",
            TipoEvaluacion = "Tarea",
            Ponderacion = 0.2f,
            Estado = "Activa"
        ),
        NotaMongo(
            _id = "20",
            FechaGrabacion = "2024-06-24",
            ValorNota = 90.0f,
            EstudianteId = "EST2024010",
            MateriaId = "MAT001",
            Periodo = "2024-2",
            TipoEvaluacion = "Examen Final",
            Ponderacion = 0.4f,
            Estado = "Activa"
        )
    )

    init {
        loadNotasData()
    }

    fun loadNotasData() {
        _notaState.value = _notaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Intentar cargar datos reales primero
                val notas = try {
                    notaRepository.getNotas()
                } catch (e: Exception) {
                    // Si falla, usar datos de demostración
                    notasDemo
                }

                val porMateria = try {
                    notaRepository.getNotasPorMateria()
                } catch (e: Exception) {
                    // Datos de demostración para materias
                    mapOf(
                        "Matemáticas" to 89.5f,
                        "Ciencias" to 85.0f,
                        "Historia" to 78.0f,
                        "Inglés" to 72.0f,
                        "Física" to 86.0f,
                        "Química" to 83.5f,
                        "Biología" to 88.0f,
                        "Literatura" to 75.0f,
                        "Arte" to 82.0f
                    )
                }

                val porPeriodo = try {
                    notaRepository.getNotasPorPeriodo()
                } catch (e: Exception) {
                    // Datos de demostración para períodos
                    mapOf(
                        "2024-1" to 82.3f,
                        "2024-2" to 85.7f
                    )
                }

                val promedios = try {
                    notaRepository.getPromediosGenerales()
                } catch (e: Exception) {
                    // Datos de demostración para promedios
                    mapOf(
                        "Promedio General" to 84.0f,
                        "Mejor Materia" to 89.5f,
                        "Peor Materia" to 72.0f
                    )
                }

                val estadisticas = try {
                    notaRepository.getEstadisticasNotas()
                } catch (e: Exception) {
                    // Estadísticas de demostración
                    mapOf(
                        "totalNotas" to 20,
                        "aprobados" to 16,
                        "reprobados" to 4,
                        "tasaAprobacion" to 80.0,
                        "notaMaxima" to 96.0,
                        "notaMinima" to 58.0
                    )
                }

                val alertas = try {
                    notaRepository.getAlertasRendimiento()
                } catch (e: Exception) {
                    // Alertas de demostración (notas reprobadas)
                    notasDemo.filter { it.ValorNota < 70 }
                }

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
                    error = "Error cargando notas. Usando datos de demostración"
                )

                // Fallback completo con datos demo
                _notaState.value = NotaState(
                    isLoading = false,
                    notas = notasDemo,
                    notasFiltradas = notasDemo,
                    notasPorMateria = mapOf(
                        "Matemáticas" to 89.5f, "Ciencias" to 85.0f, "Historia" to 78.0f,
                        "Inglés" to 72.0f, "Física" to 86.0f, "Química" to 83.5f,
                        "Biología" to 88.0f, "Literatura" to 75.0f, "Arte" to 82.0f
                    ),
                    notasPorPeriodo = mapOf("2024-1" to 82.3f, "2024-2" to 85.7f),
                    promediosGenerales = mapOf(
                        "Promedio General" to 84.0f, "Mejor Materia" to 89.5f, "Peor Materia" to 72.0f
                    ),
                    estadisticas = mapOf(
                        "totalNotas" to 20, "aprobados" to 16, "reprobados" to 4,
                        "tasaAprobacion" to 80.0, "notaMaxima" to 96.0, "notaMinima" to 58.0
                    ),
                    alertasRendimiento = notasDemo.filter { it.ValorNota < 70 }
                )
            }
        }
    }

    fun loadNotasPorEstudiante(estudianteId: String) {
        _notaState.value = _notaState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val notasEstudiante = try {
                    notaRepository.getNotasPorEstudiante(estudianteId)
                } catch (e: Exception) {
                    // Datos de demostración para estudiante específico
                    notasDemo.filter { it.EstudianteId == estudianteId }
                }

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
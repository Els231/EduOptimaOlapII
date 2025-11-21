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
    val promedioPorGrado: Map<String, Float> = emptyMap(),
    val promedioPorTrimestre: Map<String, Float> = emptyMap(),
    val promedioPorMunicipio: Map<String, Float> = emptyMap(),
    val error: String? = null
)

@HiltViewModel
class OLAPViewModel @Inject constructor(
    private val olapQueryService: OLAPQueryService
) : ViewModel() {

    private val _olapState = MutableStateFlow(OLAPState())
    val olapState: StateFlow<OLAPState> = _olapState.asStateFlow()

    // Datos estáticos de demostración para OLAP - CORREGIDOS con estructuras correctas
    private val dimEstudianteDemo = listOf(
        DimEstudiante(
            id = "1",
            nombre = "María García López",
            apellido = "García López",
            nombreTutor = "Carlos García",
            apellidoTutor = "García Rodríguez",
            municipio = "San Salvador",
            departamento = "San Salvador",
            sexo = "Femenino"
        ),
        DimEstudiante(
            id = "2",
            nombre = "Juan Pérez Martínez",
            apellido = "Pérez Martínez",
            nombreTutor = "Ana Pérez",
            apellidoTutor = "Pérez Hernández",
            municipio = "Santa Tecla",
            departamento = "La Libertad",
            sexo = "Masculino"
        ),
        DimEstudiante(
            id = "3",
            nombre = "Ana Rodríguez Silva",
            apellido = "Rodríguez Silva",
            nombreTutor = "Miguel Rodríguez",
            apellidoTutor = "Rodríguez Castro",
            municipio = "Soyapango",
            departamento = "San Salvador",
            sexo = "Femenino"
        ),
        DimEstudiante(
            id = "4",
            nombre = "Carlos Hernández Díaz",
            apellido = "Hernández Díaz",
            nombreTutor = "Elena Hernández",
            apellidoTutor = "Hernández Méndez",
            municipio = "Mejicanos",
            departamento = "San Salvador",
            sexo = "Masculino"
        ),
        DimEstudiante(
            id = "5",
            nombre = "Laura Martínez Cruz",
            apellido = "Martínez Cruz",
            nombreTutor = "Roberto Martínez",
            apellidoTutor = "Martínez Fuentes",
            municipio = "Apopa",
            departamento = "San Salvador",
            sexo = "Femenino"
        ),
        DimEstudiante(
            id = "6",
            nombre = "Diego González Reyes",
            apellido = "González Reyes",
            nombreTutor = "Patricia González",
            apellidoTutor = "González Ramírez",
            municipio = "San Martín",
            departamento = "San Salvador",
            sexo = "Masculino"
        ),
        DimEstudiante(
            id = "7",
            nombre = "Sofia Castro Mendoza",
            apellido = "Castro Mendoza",
            nombreTutor = "Jorge Castro",
            apellidoTutor = "Castro Alvarado",
            municipio = "Ilopango",
            departamento = "San Salvador",
            sexo = "Femenino"
        ),
        DimEstudiante(
            id = "8",
            nombre = "Miguel Ángel Ramírez",
            apellido = "Ramírez Torres",
            nombreTutor = "Carmen Ramírez",
            apellidoTutor = "Ramírez Ortiz",
            municipio = "Cuscatancingo",
            departamento = "San Salvador",
            sexo = "Masculino"
        ),
        DimEstudiante(
            id = "9",
            nombre = "Elena Morales Vásquez",
            apellido = "Morales Vásquez",
            nombreTutor = "Luis Morales",
            apellidoTutor = "Morales Jiménez",
            municipio = "Ciudad Delgado",
            departamento = "San Salvador",
            sexo = "Femenino"
        ),
        DimEstudiante(
            id = "10",
            nombre = "Roberto Silva Ortega",
            apellido = "Silva Ortega",
            nombreTutor = "María Silva",
            apellidoTutor = "Silva Navarro",
            municipio = "Antiguo Cuscatlán",
            departamento = "La Libertad",
            sexo = "Masculino"
        )
    )

    private val dimTiempoDemo = listOf(
        DimTiempo(
            IdDimTiempo = "1",
            Fecha = "2024-01-15",
            Dia = 15,
            Mes = 1,
            Ano = 2024,
            Trimestre = 1,
            Semestre = 1,
            DiaSemana = "Lunes",
            MesNombre = "Enero",
            EsFinDeSemana = false,
            EsFeriado = false
        ),
        DimTiempo(
            IdDimTiempo = "2",
            Fecha = "2024-02-20",
            Dia = 20,
            Mes = 2,
            Ano = 2024,
            Trimestre = 1,
            Semestre = 1,
            DiaSemana = "Martes",
            MesNombre = "Febrero",
            EsFinDeSemana = false,
            EsFeriado = false
        ),
        DimTiempo(
            IdDimTiempo = "3",
            Fecha = "2024-03-10",
            Dia = 10,
            Mes = 3,
            Ano = 2024,
            Trimestre = 1,
            Semestre = 1,
            DiaSemana = "Domingo",
            MesNombre = "Marzo",
            EsFinDeSemana = true,
            EsFeriado = false
        ),
        DimTiempo(
            IdDimTiempo = "4",
            Fecha = "2024-04-05",
            Dia = 5,
            Mes = 4,
            Ano = 2024,
            Trimestre = 2,
            Semestre = 1,
            DiaSemana = "Viernes",
            MesNombre = "Abril",
            EsFinDeSemana = false,
            EsFeriado = true
        ),
        DimTiempo(
            IdDimTiempo = "5",
            Fecha = "2024-05-12",
            Dia = 12,
            Mes = 5,
            Ano = 2024,
            Trimestre = 2,
            Semestre = 1,
            DiaSemana = "Domingo",
            MesNombre = "Mayo",
            EsFinDeSemana = true,
            EsFeriado = false
        ),
        DimTiempo(
            IdDimTiempo = "6",
            Fecha = "2024-06-18",
            Dia = 18,
            Mes = 6,
            Ano = 2024,
            Trimestre = 2,
            Semestre = 1,
            DiaSemana = "Martes",
            MesNombre = "Junio",
            EsFinDeSemana = false,
            EsFeriado = false
        )
    )

    private val dimCalificacionDemo = listOf(
        DimCalificacion(
            Id = "1",
            DescripcionCalificacion = "Excelente",
            Grado = "Primero",
            Profesor = "Prof. Carlos García",
            Materia = "Matemáticas",
            RangoMinimo = 90.0f,
            RangoMaximo = 100.0f,
            LetraCalificacion = "A"
        ),
        DimCalificacion(
            Id = "2",
            DescripcionCalificacion = "Muy Bueno",
            Grado = "Segundo",
            Profesor = "Prof. Ana Martínez",
            Materia = "Ciencias",
            RangoMinimo = 80.0f,
            RangoMaximo = 89.9f,
            LetraCalificacion = "B"
        ),
        DimCalificacion(
            Id = "3",
            DescripcionCalificacion = "Bueno",
            Grado = "Tercero",
            Profesor = "Prof. Miguel Rodríguez",
            Materia = "Historia",
            RangoMinimo = 70.0f,
            RangoMaximo = 79.9f,
            LetraCalificacion = "C"
        ),
        DimCalificacion(
            Id = "4",
            DescripcionCalificacion = "Regular",
            Grado = "Cuarto",
            Profesor = "Prof. Elena Hernández",
            Materia = "Inglés",
            RangoMinimo = 60.0f,
            RangoMaximo = 69.9f,
            LetraCalificacion = "D"
        ),
        DimCalificacion(
            Id = "5",
            DescripcionCalificacion = "Reprobado",
            Grado = "Quinto",
            Profesor = "Prof. Roberto López",
            Materia = "Física",
            RangoMinimo = 0.0f,
            RangoMaximo = 59.9f,
            LetraCalificacion = "F"
        )
    )

    // Crear FactNota con referencias a objetos completos
    private val factNotaDemo = listOf(
        FactNota(
            idHechos = "1",
            dimEstudiante = dimEstudianteDemo[0],
            dimCalificacion = dimCalificacionDemo[0],
            dimTiempo = dimTiempoDemo[0],
            valorNota = 95.0f
        ),
        FactNota(
            idHechos = "2",
            dimEstudiante = dimEstudianteDemo[1],
            dimCalificacion = dimCalificacionDemo[1],
            dimTiempo = dimTiempoDemo[1],
            valorNota = 88.0f
        ),
        FactNota(
            idHechos = "3",
            dimEstudiante = dimEstudianteDemo[2],
            dimCalificacion = dimCalificacionDemo[2],
            dimTiempo = dimTiempoDemo[2],
            valorNota = 82.0f
        ),
        FactNota(
            idHechos = "4",
            dimEstudiante = dimEstudianteDemo[3],
            dimCalificacion = dimCalificacionDemo[3],
            dimTiempo = dimTiempoDemo[3],
            valorNota = 75.0f
        ),
        FactNota(
            idHechos = "5",
            dimEstudiante = dimEstudianteDemo[4],
            dimCalificacion = dimCalificacionDemo[4],
            dimTiempo = dimTiempoDemo[4],
            valorNota = 68.0f
        ),
        FactNota(
            idHechos = "6",
            dimEstudiante = dimEstudianteDemo[5],
            dimCalificacion = dimCalificacionDemo[0],
            dimTiempo = dimTiempoDemo[5],
            valorNota = 92.0f
        ),
        FactNota(
            idHechos = "7",
            dimEstudiante = dimEstudianteDemo[6],
            dimCalificacion = dimCalificacionDemo[1],
            dimTiempo = dimTiempoDemo[0],
            valorNota = 85.0f
        ),
        FactNota(
            idHechos = "8",
            dimEstudiante = dimEstudianteDemo[7],
            dimCalificacion = dimCalificacionDemo[2],
            dimTiempo = dimTiempoDemo[1],
            valorNota = 79.0f
        ),
        FactNota(
            idHechos = "9",
            dimEstudiante = dimEstudianteDemo[8],
            dimCalificacion = dimCalificacionDemo[3],
            dimTiempo = dimTiempoDemo[2],
            valorNota = 73.0f
        ),
        FactNota(
            idHechos = "10",
            dimEstudiante = dimEstudianteDemo[9],
            dimCalificacion = dimCalificacionDemo[4],
            dimTiempo = dimTiempoDemo[3],
            valorNota = 55.0f
        ),
        FactNota(
            idHechos = "11",
            dimEstudiante = dimEstudianteDemo[0],
            dimCalificacion = dimCalificacionDemo[1],
            dimTiempo = dimTiempoDemo[4],
            valorNota = 87.0f
        ),
        FactNota(
            idHechos = "12",
            dimEstudiante = dimEstudianteDemo[1],
            dimCalificacion = dimCalificacionDemo[2],
            dimTiempo = dimTiempoDemo[5],
            valorNota = 81.0f
        ),
        FactNota(
            idHechos = "13",
            dimEstudiante = dimEstudianteDemo[2],
            dimCalificacion = dimCalificacionDemo[3],
            dimTiempo = dimTiempoDemo[0],
            valorNota = 76.0f
        ),
        FactNota(
            idHechos = "14",
            dimEstudiante = dimEstudianteDemo[3],
            dimCalificacion = dimCalificacionDemo[4],
            dimTiempo = dimTiempoDemo[1],
            valorNota = 62.0f
        ),
        FactNota(
            idHechos = "15",
            dimEstudiante = dimEstudianteDemo[4],
            dimCalificacion = dimCalificacionDemo[0],
            dimTiempo = dimTiempoDemo[2],
            valorNota = 94.0f
        )
    )

    init {
        loadOLAPData()
    }

    fun loadOLAPData() {
        _olapState.value = _olapState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Intentar cargar datos reales primero
                val estudiantes = try {
                    olapQueryService.getAllEstudiantes().body() ?: dimEstudianteDemo
                } catch (e: Exception) {
                    dimEstudianteDemo
                }

                val tiempos = try {
                    olapQueryService.getAllTiempos().body() ?: dimTiempoDemo
                } catch (e: Exception) {
                    dimTiempoDemo
                }

                val calificaciones = try {
                    olapQueryService.getAllCalificaciones().body() ?: dimCalificacionDemo
                } catch (e: Exception) {
                    dimCalificacionDemo
                }

                val notas = try {
                    olapQueryService.getAllFactNotas().body() ?: factNotaDemo
                } catch (e: Exception) {
                    factNotaDemo
                }

                val promedioGrado = try {
                    olapQueryService.getPromedioPorGrado().body() ?: mapOf(
                        "Primero" to 89.5f,
                        "Segundo" to 85.0f,
                        "Tercero" to 82.3f,
                        "Cuarto" to 78.7f,
                        "Quinto" to 75.2f
                    )
                } catch (e: Exception) {
                    mapOf(
                        "Primero" to 89.5f,
                        "Segundo" to 85.0f,
                        "Tercero" to 82.3f,
                        "Cuarto" to 78.7f,
                        "Quinto" to 75.2f
                    )
                }

                val promedioTrimestre = try {
                    olapQueryService.getPromedioPorTrimestre().body() ?: mapOf(
                        "Trimestre 1" to 82.5f,
                        "Trimestre 2" to 85.2f,
                        "Trimestre 3" to 87.8f,
                        "Trimestre 4" to 90.1f
                    )
                } catch (e: Exception) {
                    mapOf(
                        "Trimestre 1" to 82.5f,
                        "Trimestre 2" to 85.2f,
                        "Trimestre 3" to 87.8f,
                        "Trimestre 4" to 90.1f
                    )
                }

                val promedioMunicipio = try {
                    olapQueryService.getPromedioPorMunicipio().body() ?: mapOf(
                        "San Salvador" to 85.2f,
                        "Santa Tecla" to 87.5f,
                        "Soyapango" to 82.8f,
                        "Mejicanos" to 79.3f,
                        "Apopa" to 81.6f
                    )
                } catch (e: Exception) {
                    mapOf(
                        "San Salvador" to 85.2f,
                        "Santa Tecla" to 87.5f,
                        "Soyapango" to 82.8f,
                        "Mejicanos" to 79.3f,
                        "Apopa" to 81.6f
                    )
                }

                _olapState.value = OLAPState(
                    isLoading = false,
                    dimEstudiante = estudiantes,
                    dimTiempo = tiempos,
                    dimCalificacion = calificaciones,
                    factNota = notas,
                    promedioPorGrado = promedioGrado,
                    promedioPorTrimestre = promedioTrimestre,
                    promedioPorMunicipio = promedioMunicipio
                )
            } catch (e: Exception) {
                _olapState.value = _olapState.value.copy(
                    isLoading = false,
                    error = "Error cargando datos OLAP. Usando datos de demostración"
                )

                // Cargar datos de demostración incluso en caso de error
                _olapState.value = OLAPState(
                    isLoading = false,
                    dimEstudiante = dimEstudianteDemo,
                    dimTiempo = dimTiempoDemo,
                    dimCalificacion = dimCalificacionDemo,
                    factNota = factNotaDemo,
                    promedioPorGrado = mapOf(
                        "Primero" to 89.5f,
                        "Segundo" to 85.0f,
                        "Tercero" to 82.3f,
                        "Cuarto" to 78.7f,
                        "Quinto" to 75.2f
                    ),
                    promedioPorTrimestre = mapOf(
                        "Trimestre 1" to 82.5f,
                        "Trimestre 2" to 85.2f,
                        "Trimestre 3" to 87.8f,
                        "Trimestre 4" to 90.1f
                    ),
                    promedioPorMunicipio = mapOf(
                        "San Salvador" to 85.2f,
                        "Santa Tecla" to 87.5f,
                        "Soyapango" to 82.8f,
                        "Mejicanos" to 79.3f,
                        "Apopa" to 81.6f
                    )
                )
            }
        }
    }

    fun loadNotasCompletas() {
        viewModelScope.launch {
            try {
                val response = try {
                    olapQueryService.getAllFactNotas()
                } catch (e: Exception) {
                    null
                }

                if (response?.isSuccessful == true) {
                    _olapState.value = _olapState.value.copy(
                        factNota = response.body() ?: factNotaDemo
                    )
                } else {
                    _olapState.value = _olapState.value.copy(
                        factNota = factNotaDemo
                    )
                }
            } catch (e: Exception) {
                _olapState.value = _olapState.value.copy(
                    factNota = factNotaDemo
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
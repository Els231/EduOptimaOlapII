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

    // Datos estáticos para demostración
    private val estudiantesDemo = listOf(
        EstudianteMongo(
            _id = "1", CodigoEstudiante = "EST2024001", Nombres = "María Gabriela",
            Apellidos = "García López", FechaNacimiento = "2008-03-15", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "2", CodigoEstudiante = "EST2024002", Nombres = "Juan Carlos",
            Apellidos = "Pérez Martínez", FechaNacimiento = "2007-11-22", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "3", CodigoEstudiante = "EST2024003", Nombres = "Ana Sofía",
            Apellidos = "Rodríguez Silva", FechaNacimiento = "2008-05-30", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "4", CodigoEstudiante = "EST2024004", Nombres = "Carlos Eduardo",
            Apellidos = "Hernández Díaz", FechaNacimiento = "2007-08-14", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "5", CodigoEstudiante = "EST2024005", Nombres = "Laura Patricia",
            Apellidos = "Martínez Cruz", FechaNacimiento = "2008-01-25", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "6", CodigoEstudiante = "EST2024006", Nombres = "Diego Alejandro",
            Apellidos = "González Reyes", FechaNacimiento = "2007-12-03", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "7", CodigoEstudiante = "EST2024007", Nombres = "Sofia Isabel",
            Apellidos = "Castro Mendoza", FechaNacimiento = "2008-07-19", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "8", CodigoEstudiante = "EST2024008", Nombres = "Miguel Ángel",
            Apellidos = "Ramírez Torres", FechaNacimiento = "2007-09-11", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "9", CodigoEstudiante = "EST2024009", Nombres = "Elena Beatriz",
            Apellidos = "Morales Vásquez", FechaNacimiento = "2008-02-28", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "10", CodigoEstudiante = "EST2024010", Nombres = "Roberto José",
            Apellidos = "Silva Ortega", FechaNacimiento = "2007-10-07", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "11", CodigoEstudiante = "EST2024011", Nombres = "Carmen Lucía",
            Apellidos = "López Herrera", FechaNacimiento = "2008-04-12", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "12", CodigoEstudiante = "EST2024012", Nombres = "Fernando Antonio",
            Apellidos = "Jiménez Castro", FechaNacimiento = "2007-06-18", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "13", CodigoEstudiante = "EST2024013", Nombres = "Patricia Elena",
            Apellidos = "Navarro Ruiz", FechaNacimiento = "2008-08-22", Sexo = "Femenino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "14", CodigoEstudiante = "EST2024014", Nombres = "Jorge Luis",
            Apellidos = "Méndez Flores", FechaNacimiento = "2007-03-09", Sexo = "Masculino",
            Estado = "Activo"
        ),
        EstudianteMongo(
            _id = "15", CodigoEstudiante = "EST2024015", Nombres = "Lucía Fernanda",
            Apellidos = "Ortiz Ríos", FechaNacimiento = "2008-12-15", Sexo = "Femenino",
            Estado = "Activo"
        )
    )

    init {
        loadEstudiantesData()
    }

    fun loadEstudiantesData() {
        _estudianteState.value = _estudianteState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val estudiantes = try {
                    estudianteRepository.getEstudiantes()
                } catch (e: Exception) {
                    estudiantesDemo
                }

                val porMunicipio = try {
                    estudianteRepository.getEstudiantesPorMunicipio()
                } catch (e: Exception) {
                    mapOf(
                        "San Salvador" to 6,
                        "Santa Tecla" to 3,
                        "Soyapango" to 2,
                        "Mejicanos" to 2,
                        "Apopa" to 2
                    )
                }

                val porSexo = try {
                    estudianteRepository.getEstudiantesPorSexo()
                } catch (e: Exception) {
                    mapOf(
                        "Femenino" to 8,
                        "Masculino" to 7
                    )
                }

                val porGrado = try {
                    estudianteRepository.getEstudiantesPorGrado()
                } catch (e: Exception) {
                    mapOf(
                        "Primero" to 3,
                        "Segundo" to 4,
                        "Tercero" to 3,
                        "Cuarto" to 2,
                        "Quinto" to 2,
                        "Sexto" to 1
                    )
                }

                val estadisticas = try {
                    estudianteRepository.getEstadisticasEstudiantes()
                } catch (e: Exception) {
                    mapOf(
                        "total" to 15,
                        "activos" to 15,
                        "inactivos" to 0,
                        "promedioEdad" to 14.2
                    )
                }

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
                    error = "Error cargando estudiantes. Usando datos de demostración"
                )

                // Fallback completo con datos demo
                _estudianteState.value = EstudianteState(
                    isLoading = false,
                    estudiantes = estudiantesDemo,
                    estudiantesFiltrados = estudiantesDemo,
                    estudiantesPorMunicipio = mapOf(
                        "San Salvador" to 6, "Santa Tecla" to 3, "Soyapango" to 2,
                        "Mejicanos" to 2, "Apopa" to 2
                    ),
                    estudiantesPorSexo = mapOf("Femenino" to 8, "Masculino" to 7),
                    estudiantesPorGrado = mapOf(
                        "Primero" to 3, "Segundo" to 4, "Tercero" to 3,
                        "Cuarto" to 2, "Quinto" to 2, "Sexto" to 1
                    ),
                    estadisticas = mapOf(
                        "total" to 15, "activos" to 15, "inactivos" to 0, "promedioEdad" to 14.2
                    )
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
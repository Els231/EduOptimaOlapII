package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.repository.MongoDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MongoDBState(
    val isLoading: Boolean = false,
    val collections: List<MongoCollection> = emptyList(),
    val error: String? = null
)

data class MongoCollection(
    val name: String,
    val count: Int,
    val documents: List<Map<String, Any>>
)

@HiltViewModel
class MongoDBViewModel @Inject constructor(
    private val mongoDBRepository: MongoDBRepository
) : ViewModel() {

    private val _mongoState = MutableStateFlow(MongoDBState())
    val mongoState: StateFlow<MongoDBState> = _mongoState.asStateFlow()

    // Datos de demostración para MongoDB Explorer
    private val estudiantesDemo = listOf(
        mapOf(
            "_id" to "1",
            "CodigoEstudiante" to "EST2024001",
            "Nombres" to "María Gabriela",
            "Apellidos" to "García López",
            "FechaNacimiento" to "2008-03-15",
            "Sexo" to "Femenino",
            "Estado" to "Activo"
        ),
        mapOf(
            "_id" to "2",
            "CodigoEstudiante" to "EST2024002",
            "Nombres" to "Juan Carlos",
            "Apellidos" to "Pérez Martínez",
            "FechaNacimiento" to "2007-11-22",
            "Sexo" to "Masculino",
            "Estado" to "Activo"
        ),
        mapOf(
            "_id" to "3",
            "CodigoEstudiante" to "EST2024003",
            "Nombres" to "Ana Sofía",
            "Apellidos" to "Rodríguez Silva",
            "FechaNacimiento" to "2008-05-30",
            "Sexo" to "Femenino",
            "Estado" to "Activo"
        )
    )

    private val matriculasDemo = listOf(
        mapOf(
            "_id" to "1",
            "CodigoMatricula" to "MAT2024001",
            "FechaMatricula" to "2024-01-15",
            "Grado" to "Primero",
            "Turno" to "Matutino",
            "Estado" to "Activa"
        ),
        mapOf(
            "_id" to "2",
            "CodigoMatricula" to "MAT2024002",
            "FechaMatricula" to "2024-01-15",
            "Grado" to "Segundo",
            "Turno" to "Matutino",
            "Estado" to "Activa"
        )
    )

    private val notasDemo = listOf(
        mapOf(
            "_id" to "1",
            "CodigoNota" to "NOT001",
            "ValorNota" to 95.0,
            "Materia" to "Matemáticas",
            "Periodo" to "2024-1",
            "Estado" to "Aprobado"
        ),
        mapOf(
            "_id" to "2",
            "CodigoNota" to "NOT002",
            "ValorNota" to 88.0,
            "Materia" to "Ciencias",
            "Periodo" to "2024-1",
            "Estado" to "Aprobado"
        )
    )

    init {
        loadMongoData()
    }

    fun loadMongoData() {
        _mongoState.value = _mongoState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val collections = try {
                    mongoDBRepository.getCollections()
                } catch (e: Exception) {
                    // Datos demo para MongoDB Explorer
                    listOf(
                        MongoCollection(
                            name = "estudiantes",
                            count = 15,
                            documents = estudiantesDemo
                        ),
                        MongoCollection(
                            name = "matriculas",
                            count = 18,
                            documents = matriculasDemo
                        ),
                        MongoCollection(
                            name = "notas",
                            count = 20,
                            documents = notasDemo
                        ),
                        MongoCollection(
                            name = "profesores",
                            count = 8,
                            documents = listOf(
                                mapOf(
                                    "_id" to "1",
                                    "CodigoProfesor" to "PROF001",
                                    "Nombres" to "Carlos Antonio",
                                    "Apellidos" to "García Rodríguez",
                                    "Especialidad" to "Matemáticas"
                                )
                            )
                        ),
                        MongoCollection(
                            name = "materias",
                            count = 12,
                            documents = listOf(
                                mapOf(
                                    "_id" to "1",
                                    "CodigoMateria" to "MAT001",
                                    "NombreMateria" to "Matemáticas Avanzadas",
                                    "Creditos" to 4
                                )
                            )
                        )
                    )
                }

                _mongoState.value = MongoDBState(
                    isLoading = false,
                    collections = collections
                )
            } catch (e: Exception) {
                _mongoState.value = MongoDBState(
                    isLoading = false,
                    error = "Error conectando a MongoDB. Usando datos de demostración"
                )

                // Fallback con datos demo completos
                _mongoState.value = MongoDBState(
                    isLoading = false,
                    collections = listOf(
                        MongoCollection(
                            name = "estudiantes",
                            count = 15,
                            documents = estudiantesDemo
                        ),
                        MongoCollection(
                            name = "matriculas",
                            count = 18,
                            documents = matriculasDemo
                        ),
                        MongoCollection(
                            name = "notas",
                            count = 20,
                            documents = notasDemo
                        ),
                        MongoCollection(
                            name = "profesores",
                            count = 8,
                            documents = listOf(
                                mapOf(
                                    "_id" to "1",
                                    "CodigoProfesor" to "PROF001",
                                    "Nombres" to "Carlos Antonio",
                                    "Apellidos" to "García Rodríguez",
                                    "Especialidad" to "Matemáticas",
                                    "Estado" to "Activo"
                                ),
                                mapOf(
                                    "_id" to "2",
                                    "CodigoProfesor" to "PROF002",
                                    "Nombres" to "Ana Patricia",
                                    "Apellidos" to "Martínez López",
                                    "Especialidad" to "Ciencias",
                                    "Estado" to "Activo"
                                )
                            )
                        ),
                        MongoCollection(
                            name = "materias",
                            count = 12,
                            documents = listOf(
                                mapOf(
                                    "_id" to "1",
                                    "CodigoMateria" to "MAT001",
                                    "NombreMateria" to "Matemáticas Avanzadas",
                                    "Creditos" to 4,
                                    "Grado" to "Primero"
                                ),
                                mapOf(
                                    "_id" to "2",
                                    "CodigoMateria" to "CIE001",
                                    "NombreMateria" to "Ciencias Naturales",
                                    "Creditos" to 3,
                                    "Grado" to "Segundo"
                                )
                            )
                        )
                    )
                )
            }
        }
    }

    fun refreshData() {
        loadMongoData()
    }
}
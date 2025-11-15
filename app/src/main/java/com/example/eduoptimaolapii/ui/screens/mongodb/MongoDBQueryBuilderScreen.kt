package com.example.eduoptimaolapii.ui.screens.mongodb

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eduoptimaolapii.ui.components.ErrorState
import com.example.eduoptimaolapii.ui.viewmodels.EstudianteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MongoDBQueryBuilderScreen(
    onBack: () -> Unit
) {
    val estudianteViewModel: EstudianteViewModel = hiltViewModel()
    val estudianteState by estudianteViewModel.estudianteState.collectAsState()

    LaunchedEffect(Unit) {
        estudianteViewModel.loadEstudiantesData()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🔍 Constructor de Consultas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { estudianteViewModel.refreshData() }
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                estudianteState.isLoading -> {
                    LoadingQueryBuilder()
                }
                estudianteState.error != null -> {
                    ErrorState(
                        error = estudianteState.error!!,
                        onRetry = { estudianteViewModel.refreshData() }
                    )
                }
                else -> {
                    QueryBuilderContent(estudianteState, estudianteViewModel)
                }
            }
        }
    }
}

@Composable
fun LoadingQueryBuilder() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Cargando constructor de consultas...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QueryBuilderContent(
    state: com.example.eduoptimaolapii.ui.viewmodels.EstudianteState,
    viewModel: EstudianteViewModel
) {
    var campoSeleccionado by remember { mutableStateOf("Nombres") }
    var operadorSeleccionado by remember { mutableStateOf("Contiene") }
    var valorBusqueda by remember { mutableStateOf("") }
    var resultadoConsulta by remember { mutableStateOf<List<String>>(emptyList()) }
    var mostrarMenuCampo by remember { mutableStateOf(false) }
    var mostrarMenuOperador by remember { mutableStateOf(false) }

    val camposDisponibles = listOf("Nombres", "Apellidos", "CodigoEstudiante", "Sexo", "FechaNacimiento")
    val operadoresDisponibles = listOf("Contiene", "Igual a", "Comienza con", "Termina con")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "🔍 Constructor de Consultas MongoDB",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Crea consultas personalizadas para la base de datos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // CONSTRUCTOR DE CONSULTA
        item {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.QueryBuilder,
                            contentDescription = "Constructor",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Construir Consulta",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // SELECTOR DE CAMPO
                    ExposedDropdownMenuBox(
                        expanded = mostrarMenuCampo,
                        onExpandedChange = { mostrarMenuCampo = !mostrarMenuCampo }
                    ) {
                        OutlinedTextField(
                            value = campoSeleccionado,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            label = { Text("Campo a buscar") },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = mostrarMenuCampo)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = mostrarMenuCampo,
                            onDismissRequest = { mostrarMenuCampo = false }
                        ) {
                            camposDisponibles.forEach { campo ->
                                DropdownMenuItem(
                                    text = { Text(campo) },
                                    onClick = {
                                        campoSeleccionado = campo
                                        mostrarMenuCampo = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // SELECTOR DE OPERADOR
                    ExposedDropdownMenuBox(
                        expanded = mostrarMenuOperador,
                        onExpandedChange = { mostrarMenuOperador = !mostrarMenuOperador }
                    ) {
                        OutlinedTextField(
                            value = operadorSeleccionado,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            label = { Text("Operador de búsqueda") },
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = mostrarMenuOperador)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = mostrarMenuOperador,
                            onDismissRequest = { mostrarMenuOperador = false }
                        ) {
                            operadoresDisponibles.forEach { operador ->
                                DropdownMenuItem(
                                    text = { Text(operador) },
                                    onClick = {
                                        operadorSeleccionado = operador
                                        mostrarMenuOperador = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // VALOR DE BÚSQUEDA
                    OutlinedTextField(
                        value = valorBusqueda,
                        onValueChange = { valorBusqueda = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Valor a buscar") },
                        placeholder = { Text("Ingrese el valor...") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // BOTÓN EJECUTAR
                    Button(
                        onClick = {
                            viewModel.searchEstudiantes(valorBusqueda)
                            resultadoConsulta = state.estudiantesFiltrados.map {
                                "${it.Nombres} ${it.Apellidos} - ${it.CodigoEstudiante}"
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = valorBusqueda.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Default.PlayArrow,
                            contentDescription = "Ejecutar",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text("Ejecutar Consulta")
                    }
                }
            }
        }

        // RESULTADOS DE LA CONSULTA
        item {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "📋 Resultados de la Consulta",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (resultadoConsulta.isEmpty()) {
                        Text(
                            text = "Ejecute una consulta para ver los resultados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    } else {
                        Column {
                            resultadoConsulta.forEachIndexed { index, resultado ->
                                Text(
                                    text = "${index + 1}. $resultado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Total: ${resultadoConsulta.size} registros encontrados",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // EJEMPLOS DE CONSULTAS
        item {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "💡 Ejemplos de Consultas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val ejemplos = listOf(
                        "Campo: Nombres, Operador: Contiene, Valor: Maria",
                        "Campo: Sexo, Operador: Igual a, Valor: Femenino",
                        "Campo: CodigoEstudiante, Operador: Comienza con, Valor: EST",
                        "Campo: Apellidos, Operador: Termina con, Valor: ez"
                    )

                    ejemplos.forEachIndexed { index, ejemplo ->
                        Text(
                            text = "${index + 1}. $ejemplo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
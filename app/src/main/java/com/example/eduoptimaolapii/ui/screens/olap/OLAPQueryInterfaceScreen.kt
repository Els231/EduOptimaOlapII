package com.example.eduoptimaolapii.ui.screens.olap

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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.eduoptimaolapii.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OLAPQueryInterfaceScreen(
    onBack: () -> Unit
) {
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val dashboardState by dashboardViewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        dashboardViewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "💻 Interfaz de Consultas OLAP",
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
                        onClick = { dashboardViewModel.refreshData() }
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
                dashboardState.isLoading -> {
                    LoadingQueryInterface()
                }
                dashboardState.dashboardResumen != null -> {
                    QueryInterfaceContent(dashboardState)
                }
                else -> {
                    ErrorState(
                        error = dashboardState.error ?: "Error desconocido",
                        onRetry = { dashboardViewModel.refreshData() }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingQueryInterface() {
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
            "Cargando interfaz de consultas OLAP...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun QueryInterfaceContent(dashboardState: com.example.eduoptimaolapii.ui.viewmodels.DashboardState) {
    var queryText by remember { mutableStateOf("") }
    var queryResults by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedQueryTemplate by remember { mutableStateOf("") }

    val queryTemplates = listOf(
        "SELECT [Measures].[Total Estudiantes] ON COLUMNS FROM [Cubo Educativo]",
        "SELECT {[DimTiempo].[2024].Children} ON ROWS, [Measures].[Promedio Notas] ON COLUMNS FROM [Cubo Educativo]",
        "SELECT [DimEstudiante].[Sexo].Members ON ROWS, [Measures].[Matrículas] ON COLUMNS FROM [Cubo Educativo]",
        "SELECT {[DimCalificacion].[Aprobado], [DimCalificacion].[Reprobado]} ON ROWS, [Measures].[Cantidad] ON COLUMNS FROM [Cubo Educativo] WHERE [DimTiempo].[2024]"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "🔍 Interfaz de Consultas MDX/OLAP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Ejecuta consultas multidimensionales en el cubo OLAP",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // EDITOR DE CONSULTAS
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
                            Icons.Default.Code,
                            contentDescription = "Editor",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "Editor de Consultas MDX",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = queryText,
                        onValueChange = { queryText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        label = { Text("Escriba su consulta MDX aquí...") },
                        placeholder = { Text("SELECT [Measures] ON COLUMNS FROM [Cubo]") },
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                // Simular ejecución de consulta
                                queryResults = listOf(
                                    "Consulta ejecutada exitosamente",
                                    "Dimensión: ${if (queryText.contains("DimEstudiante")) "Estudiante" else "General"}",
                                    "Medida: ${if (queryText.contains("Promedio")) "Promedio" else "Conteo"}",
                                    "Resultados: ${dashboardState.dashboardResumen?.totalEstudiantes ?: 0} registros"
                                )
                            },
                            modifier = Modifier.weight(1f),
                            enabled = queryText.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Ejecutar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text("Ejecutar")
                        }

                        Button(
                            onClick = { queryText = "" },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Limpiar",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            Text("Limpiar")
                        }
                    }
                }
            }
        }

        // PLANTILLAS DE CONSULTAS
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
                            contentDescription = "Plantillas",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📋 Plantillas de Consultas MDX",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    queryTemplates.forEachIndexed { index, template ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = {
                                queryText = template
                                selectedQueryTemplate = "Plantilla ${index + 1}"
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Usar plantilla",
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.padding(8.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "Plantilla ${index + 1}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = template,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // RESULTADOS DE CONSULTA
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
                            Icons.Default.Analytics,
                            contentDescription = "Resultados",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📊 Resultados de la Consulta",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (queryResults.isEmpty()) {
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
                            queryResults.forEachIndexed { index, resultado ->
                                Text(
                                    text = "• $resultado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Consulta: $selectedQueryTemplate",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // INFORMACIÓN DEL CUBO
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
                        text = "ℹ️ Estructura del Cubo OLAP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    CubeDimensionRow("DimEstudiante", "Información de estudiantes")
                    CubeDimensionRow("Fact_Nota", "Hechos de calificaciones")
                    CubeDimensionRow("DimTiempo", "Dimensión temporal")
                    CubeDimensionRow("DimCalificacion", "Dimensión de calificaciones")

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Medidas disponibles: [Total Estudiantes], [Promedio Notas], [Matrículas], [Tasa Aprobación]",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun CubeDimensionRow(dimension: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = dimension,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "✅",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
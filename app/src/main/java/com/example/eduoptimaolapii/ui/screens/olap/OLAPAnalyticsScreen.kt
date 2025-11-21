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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalBarChart
import com.example.eduoptimaolapii.ui.components.charts.PieChartComponent
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalLineChart
import com.example.eduoptimaolapii.ui.viewmodels.OLAPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OLAPAnalyticsScreen(
    onBack: () -> Unit,
    onNavigateToAdvanced: () -> Unit,
    onNavigateToQuery: () -> Unit
) {
    val olapViewModel: OLAPViewModel = hiltViewModel() // ✅ CAMBIADO A OLAPViewModel
    val olapState by olapViewModel.olapState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        olapViewModel.loadOLAPData() // ✅ CARGA DATOS OLAP REALES
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "📊 Analytics OLAP",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAdvanced) {
                        Icon(Icons.Default.Analytics, contentDescription = "Avanzadas")
                    }
                    IconButton(onClick = onNavigateToQuery) {
                        Icon(Icons.Default.Search, contentDescription = "Consultas")
                    }
                    IconButton(
                        onClick = { olapViewModel.refreshData() }
                    ) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = "Actualizar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                olapState.isLoading -> {
                    LoadingOLAPAnalytics()
                }
                olapState.error != null -> {
                    ErrorOLAPAnalytics(
                        error = olapState.error!!,
                        onRetry = { olapViewModel.refreshData() }
                    )
                }
                else -> {
                    OLAPAnalyticsContent(olapState)
                }
            }
        }
    }
}

@Composable
fun LoadingOLAPAnalytics() {
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
            "Cargando analíticas OLAP...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ErrorOLAPAnalytics(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "❌ Error de Conexión OLAP",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry
        ) {
            Text("Reintentar Conexión")
        }
    }
}

@Composable
fun OLAPAnalyticsContent(olapState: com.example.eduoptimaolapii.ui.viewmodels.OLAPState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Análisis Cubo OLAP - Datos Reales",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Datos dimensionales desde Fact_Nota OLAP",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ✅ DIMESTUDIANTE - DATOS REALES
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
                            Icons.Default.BarChart,
                            contentDescription = "DimEstudiante",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "👥 DimEstudiante",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val totalEstudiantes = olapState.dimEstudiante.size
                    ProfessionalBarChart(
                        data = mapOf("Total Estudiantes" to totalEstudiantes.toFloat()),
                        title = "Estudiantes en Cubo OLAP"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "📊 ${totalEstudiantes} estudiantes cargados",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // ✅ FACT_NOTA - DATOS REALES
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
                            Icons.Default.PieChart,
                            contentDescription = "Fact_Nota",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📈 Fact_Nota - Rendimiento",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    val totalNotas = olapState.factNota.size
                    val promedioGeneral = olapState.promedioPorGrado.values.average().toFloat()

                    val distribucionNotas = if (totalNotas > 0) {
                        mapOf(
                            "Notas Registradas" to totalNotas.toFloat(),
                            "Promedio General" to promedioGeneral
                        )
                    } else {
                        mapOf(
                            "Sin Datos" to 1f
                        )
                    }

                    PieChartComponent(
                        data = distribucionNotas,
                        title = "Distribución de Notas"
                    )
                }
            }
        }

        // ✅ DIMTIEMPO - DATOS REALES
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
                            Icons.Default.TrendingUp,
                            contentDescription = "DimTiempo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📅 DimTiempo",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (olapState.promedioPorTrimestre.isNotEmpty()) {
                        ProfessionalLineChart(
                            data = olapState.promedioPorTrimestre,
                            title = "Promedio por Trimestre"
                        )
                    } else {
                        Text(
                            text = "No hay datos temporales disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // ✅ DIMCALIFICACION - DATOS REALES
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
                            contentDescription = "DimCalificacion",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "🎯 DimCalificacion - Por Grado",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (olapState.promedioPorGrado.isNotEmpty()) {
                        ProfessionalBarChart(
                            data = olapState.promedioPorGrado,
                            title = "Promedio por Grado"
                        )
                    } else {
                        Text(
                            text = "No hay datos de calificaciones por grado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // ✅ RESUMEN ESTADÍSTICO OLAP
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
                        text = "📈 Resumen Cubo OLAP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OLAPMetricRow("DimEstudiante", olapState.dimEstudiante.size.toString())
                    OLAPMetricRow("Fact_Nota", olapState.factNota.size.toString())
                    OLAPMetricRow("DimTiempo", olapState.dimTiempo.size.toString())
                    OLAPMetricRow("DimCalificacion", olapState.dimCalificacion.size.toString())

                    val avgGrado = if (olapState.promedioPorGrado.isNotEmpty())
                        olapState.promedioPorGrado.values.average() else 0.0
                    OLAPMetricRow("Promedio General", String.format("%.1f", avgGrado))

                    val avgMunicipio = if (olapState.promedioPorMunicipio.isNotEmpty())
                        olapState.promedioPorMunicipio.values.average() else 0.0
                    OLAPMetricRow("Prom Municipio", String.format("%.1f", avgMunicipio))
                }
            }
        }
    }
}

@Composable
fun OLAPMetricRow(metric: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = metric,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
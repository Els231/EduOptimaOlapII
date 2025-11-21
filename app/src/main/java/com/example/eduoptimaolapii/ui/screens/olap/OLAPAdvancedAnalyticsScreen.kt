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
import androidx.compose.material.icons.filled.MultilineChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.StackedBarChart
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eduoptimaolapii.ui.components.ErrorState
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalBarChart
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalLineChart
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalPieChart
import com.example.eduoptimaolapii.ui.viewmodels.OLAPViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OLAPAdvancedAnalyticsScreen(
    onBack: () -> Unit
) {
    val olapViewModel: OLAPViewModel = hiltViewModel() // ✅ CAMBIADO A OLAPViewModel
    val olapState by olapViewModel.olapState.collectAsState()

    LaunchedEffect(Unit) {
        olapViewModel.loadOLAPData() // ✅ CARGA DATOS OLAP REALES
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🚀 Analytics OLAP Avanzadas",
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
                        onClick = { olapViewModel.refreshData() }
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
                olapState.isLoading -> {
                    LoadingAdvancedAnalytics()
                }
                olapState.error != null -> {
                    ErrorState(
                        error = olapState.error!!,
                        onRetry = { olapViewModel.refreshData() }
                    )
                }
                else -> {
                    AdvancedAnalyticsContent(olapState)
                }
            }
        }
    }
}

@Composable
fun LoadingAdvancedAnalytics() {
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
            "Cargando analíticas avanzadas OLAP...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun AdvancedAnalyticsContent(olapState: com.example.eduoptimaolapii.ui.viewmodels.OLAPState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "📊 Análisis Multidimensional OLAP",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Datos dimensionales del cubo para análisis educativo avanzado",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // DIMENSIÓN ESTUDIANTE - ANÁLISIS MULTIDIMENSIONAL
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
                            Icons.Default.StackedBarChart,
                            contentDescription = "DimEstudiante",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "🎓 DimEstudiante - Análisis Multidimensional",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DimensionCard(
                            title = "👥 Total Estudiantes",
                            value = olapState.dimEstudiante.size.toString(),
                            modifier = Modifier.weight(1f)
                        )
                        DimensionCard(
                            title = "📚 Notas Registradas",
                            value = olapState.factNota.size.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // FACT_NOTA - ANÁLISIS DE RENDIMIENTO
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
                            Icons.Default.MultilineChart,
                            contentDescription = "Fact_Nota",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📈 Fact_Nota - Análisis de Rendimiento",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (olapState.promedioPorGrado.isNotEmpty()) {
                        ProfessionalLineChart(
                            data = olapState.promedioPorGrado,
                            title = "Evolución del Rendimiento por Grado"
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            val avgGrado = if (olapState.promedioPorGrado.isNotEmpty())
                                olapState.promedioPorGrado.values.average() else 0.0
                            DimensionCard(
                                title = "⭐ Promedio General",
                                value = String.format("%.1f", avgGrado),
                                modifier = Modifier.weight(1f)
                            )
                            DimensionCard(
                                title = "📊 Grados Evaluados",
                                value = olapState.promedioPorGrado.size.toString(),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    } else {
                        Text(
                            text = "No hay datos de rendimiento disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // DIMTIEMPO - ANÁLISIS TEMPORAL
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
                            contentDescription = "DimTiempo",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📅 DimTiempo - Análisis Temporal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (olapState.promedioPorTrimestre.isNotEmpty()) {
                        ProfessionalBarChart(
                            data = olapState.promedioPorTrimestre,
                            title = "Promedio por Trimestre - Evolución Temporal"
                        )
                    } else {
                        Text(
                            text = "No hay datos temporales disponibles",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // DIMCALIFICACION - ANÁLISIS DE CALIFICACIONES
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
                            contentDescription = "DimCalificacion",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "🎯 DimCalificacion - Distribución",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Datos reales del cubo OLAP
                    val distribucionCalificaciones = if (olapState.promedioPorGrado.isNotEmpty()) {
                        olapState.promedioPorGrado.mapValues { it.value }
                    } else {
                        mapOf("Sin Datos" to 0f)
                    }

                    ProfessionalPieChart(
                        data = distribucionCalificaciones,
                    )
                }
            }
        }

        // MÉTRICAS AVANZADAS DEL CUBO
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
                        text = "🔍 Métricas Avanzadas del Cubo OLAP",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    AdvancedMetricRow(
                        dimension = "DimEstudiante",
                        metric = "Total de Estudiantes",
                        value = olapState.dimEstudiante.size.toString()
                    )
                    AdvancedMetricRow(
                        dimension = "Fact_Nota",
                        metric = "Notas Registradas",
                        value = olapState.factNota.size.toString()
                    )
                    AdvancedMetricRow(
                        dimension = "DimTiempo",
                        metric = "Períodos Temporales",
                        value = olapState.dimTiempo.size.toString()
                    )
                    AdvancedMetricRow(
                        dimension = "DimCalificacion",
                        metric = "Tipos Calificación",
                        value = olapState.dimCalificacion.size.toString()
                    )

                    val avgGrado = if (olapState.promedioPorGrado.isNotEmpty())
                        olapState.promedioPorGrado.values.average() else 0.0
                    AdvancedMetricRow(
                        dimension = "Fact_Nota",
                        metric = "Promedio General",
                        value = String.format("%.1f", avgGrado)
                    )
                }
            }
        }
    }
}

@Composable
fun DimensionCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun AdvancedMetricRow(
    dimension: String,
    metric: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = metric,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = dimension,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
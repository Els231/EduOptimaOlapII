package com.example.eduoptimaolapii.ui.screens.notas

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
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
import com.example.eduoptimaolapii.ui.components.DashboardCard
import com.example.eduoptimaolapii.ui.components.ErrorState
import com.example.eduoptimaolapii.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasScreen(
    onBack: () -> Unit,
    onNavigateToAnalytics: () -> Unit
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
                        "📊 Registro Académico",
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
                        onClick = { onNavigateToAnalytics() }
                    ) {
                        Icon(
                            Icons.Default.TrendingUp,
                            contentDescription = "Analíticas",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
                    LoadingNotas()
                }
                dashboardState.dashboardResumen != null -> {
                    NotasContent(dashboardState)
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
fun LoadingNotas() {
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
            "Cargando datos académicos...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun NotasContent(dashboardState: com.example.eduoptimaolapii.ui.viewmodels.DashboardState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TARJETAS PRINCIPALES
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(
                title = "⭐ Promedio General",
                value = String.format("%.1f", dashboardState.dashboardResumen?.promedioGeneral ?: 0f),
                subtitle = "Rendimiento académico",
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Grade,
                gradientColors = listOf(
                    androidx.compose.ui.graphics.Color(0xFFfa709a),
                    androidx.compose.ui.graphics.Color(0xFFfee140)
                )
            )

            DashboardCard(
                title = "📈 Tasa de Aprobación",
                value = String.format("%.1f%%", dashboardState.dashboardResumen?.tasaAprobacion ?: 0f),
                subtitle = "Porcentaje de aprobados",
                modifier = Modifier.weight(1f),
                icon = Icons.Default.TrendingUp,
                gradientColors = listOf(
                    androidx.compose.ui.graphics.Color(0xFF43e97b),
                    androidx.compose.ui.graphics.Color(0xFF38f9d7)
                )
            )
        }

        // RENDIMIENTO POR GRADO
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Grade,
                        contentDescription = "Rendimiento",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "🎯 Rendimiento por Grado",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (dashboardState.rendimientoPorGrado.isNotEmpty()) {
                    Column {
                        dashboardState.rendimientoPorGrado.forEach { (grado, promedio) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = grado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = String.format("%.1f", promedio),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        promedio >= 90 -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                                        promedio >= 80 -> androidx.compose.ui.graphics.Color(0xFF8BC34A)
                                        promedio >= 70 -> androidx.compose.ui.graphics.Color(0xFFCDDC39)
                                        else -> androidx.compose.ui.graphics.Color(0xFFF44336)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No hay datos de rendimiento disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }

        // ALERTAS DE RENDIMIENTO
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "⚠️ Estado del Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                StatusRow("Alertas de Rendimiento", dashboardState.dashboardResumen?.alertasRendimiento?.toString() ?: "0")
                StatusRow("Eventos Próximos", dashboardState.dashboardResumen?.eventosProximos?.toString() ?: "0")
                StatusRow("Última Actualización", dashboardState.dashboardResumen?.ultimaActualizacion ?: "N/A")
            }
        }
    }
}

@Composable
fun StatusRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = when {
                label.contains("Alertas") && value != "0" -> MaterialTheme.colorScheme.error
                label.contains("Eventos") && value != "0" -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}
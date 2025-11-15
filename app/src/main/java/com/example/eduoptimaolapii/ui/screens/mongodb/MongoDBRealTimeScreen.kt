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
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
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
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MongoDBRealTimeScreen(
    onBack: () -> Unit
) {
    val estudianteViewModel: EstudianteViewModel = hiltViewModel()
    val estudianteState by estudianteViewModel.estudianteState.collectAsState()
    var lastUpdate by remember { mutableStateOf("") }
    var refreshCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        estudianteViewModel.loadEstudiantesData()
    }

    LaunchedEffect(refreshCount) {
        while (true) {
            delay(5000) // Actualizar cada 5 segundos
            lastUpdate = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
            // En una implementación real, aquí se haría una llamada a una API de WebSocket o similar
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "⚡ Monitoreo Tiempo Real",
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
                        onClick = {
                            estudianteViewModel.refreshData()
                            refreshCount++
                        }
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
                    LoadingRealTime()
                }
                estudianteState.error != null -> {
                    ErrorState(
                        error = estudianteState.error!!,
                        onRetry = { estudianteViewModel.refreshData() }
                    )
                }
                else -> {
                    RealTimeContent(estudianteState, lastUpdate)
                }
            }
        }
    }
}

@Composable
fun LoadingRealTime() {
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
            "Conectando con MongoDB en tiempo real...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun RealTimeContent(
    state: com.example.eduoptimaolapii.ui.viewmodels.EstudianteState,
    lastUpdate: String
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "📊 Monitoreo en Tiempo Real - MongoDB",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Datos actualizados automáticamente desde la base de datos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ESTADO DE CONEXIÓN
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
                            Icons.Default.LiveTv,
                            contentDescription = "Estado Conexión",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "🔌 Estado de Conexión",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    ConnectionStatusRow(
                        status = "Conectado",
                        isOnline = true,
                        lastUpdate = lastUpdate
                    )
                }
            }
        }

        // MÉTRICAS EN TIEMPO REAL
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
                            Icons.Default.Speed,
                            contentDescription = "Métricas",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "📈 Métricas en Tiempo Real",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RealTimeMetricCard(
                            title = "Total Estudiantes",
                            value = state.estudiantes.size.toString(),
                            trend = "↗️"
                        )
                        RealTimeMetricCard(
                            title = "Hombres",
                            value = state.estudiantesPorSexo["Masculino"]?.toString() ?: "0",
                            trend = "➡️"
                        )
                        RealTimeMetricCard(
                            title = "Mujeres",
                            value = state.estudiantesPorSexo["Femenino"]?.toString() ?: "0",
                            trend = "↗️"
                        )
                    }
                }
            }
        }

        // ACTIVIDAD RECIENTE
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
                            Icons.Default.Schedule,
                            contentDescription = "Actividad",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(
                            text = "🕐 Actividad Reciente",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulación de actividad reciente
                    val actividades = listOf(
                        "Consulta de estudiantes ejecutada",
                        "Datos de matrículas actualizados",
                        "Estadísticas de rendimiento calculadas",
                        "Conexión MongoDB verificada",
                        "Cache de datos actualizado"
                    )

                    actividades.forEachIndexed { index, actividad ->
                        ActivityRow(
                            activity = actividad,
                            time = "Hace ${(index + 1) * 2} min",
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // ESTADÍSTICAS DE RENDIMIENTO
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
                        text = "⚡ Rendimiento del Sistema",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    PerformanceRow("Tiempo de respuesta", "125ms", "Óptimo")
                    PerformanceRow("Consultas por minuto", "45", "Normal")
                    PerformanceRow("Uso de memoria", "68%", "Estable")
                    PerformanceRow("Conexiones activas", "3", "Bajo")
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusRow(
    status: String,
    isOnline: Boolean,
    lastUpdate: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Estado: $status",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
            Text(
                text = "Última actualización: $lastUpdate",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = if (isOnline) androidx.compose.ui.graphics.Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(6.dp)
                )
        )
    }
}

@Composable
fun RealTimeMetricCard(
    title: String,
    value: String,
    trend: String,
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
            modifier = Modifier.padding(12.dp),
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
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = trend,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ActivityRow(
    activity: String,
    time: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = activity,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun PerformanceRow(
    metric: String,
    value: String,
    status: String
) {
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
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.bodySmall,
                color = when (status) {
                    "Óptimo" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                    "Normal" -> MaterialTheme.colorScheme.primary
                    "Estable" -> MaterialTheme.colorScheme.secondary
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

// Función auxiliar para background
@Composable
private fun Modifier.background(color: androidx.compose.ui.graphics.Color, shape: androidx.compose.ui.graphics.Shape): Modifier {
    return this.then(
        androidx.compose.foundation.background(color = color, shape = shape)
    )
}
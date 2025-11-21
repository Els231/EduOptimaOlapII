package com.example.eduoptimaolapii.ui.screens.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eduoptimaolapii.ui.components.DashboardCard
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalBarChart
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalLineChart
import com.example.eduoptimaolapii.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToEstudiantes: () -> Unit,
    onNavigateToMatriculas: () -> Unit,
    onNavigateToNotas: () -> Unit,
    onNavigateToMongoDB: () -> Unit,
    onNavigateToOLAP: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: DashboardViewModel = hiltViewModel()
    val dashboardState = viewModel.dashboardState.collectAsState().value
    val isRefreshing = viewModel.isRefreshing.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadDashboardData()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🎓 Dashboard EduOptima" + if (dashboardState.isUsingDemoData) " (Demo)" else "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    ConnectionStatusIndicator(
                        isConnected = !dashboardState.isUsingDemoData,
                        isLoading = dashboardState.isLoading
                    )

                    IconButton(
                        onClick = { viewModel.refreshData() },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = if (isRefreshing) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.refreshData() },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            ) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                dashboardState.isLoading -> {
                    LoadingScreen()
                }
                dashboardState.dashboardResumen != null || dashboardState.isUsingDemoData -> {
                    DashboardContent(
                        dashboardState = dashboardState,
                        onNavigateToEstudiantes = onNavigateToEstudiantes,
                        onNavigateToMatriculas = onNavigateToMatriculas,
                        onNavigateToNotas = onNavigateToNotas,
                        onNavigateToMongoDB = onNavigateToMongoDB,
                        onNavigateToOLAP = onNavigateToOLAP,
                        onLogout = onLogout,
                        onRefresh = { viewModel.refreshData() },
                        isRefreshing = isRefreshing
                    )
                }
                else -> {
                    ErrorScreen(
                        error = dashboardState.error ?: "No se pudo conectar con los servidores",
                        onRetry = { viewModel.refreshData() }
                    )
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusIndicator(isConnected: Boolean, isLoading: Boolean) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
            isConnected -> Icon(
                Icons.Default.Analytics,
                contentDescription = "Conectado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
            else -> Icon(
                Icons.Outlined.WifiOff,
                contentDescription = "Modo Demo",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LoadingScreen() {
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
            "Conectando con servidores...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "OLAP + MongoDB",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ErrorScreen(
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
        Icon(
            Icons.Outlined.WifiOff,
            contentDescription = "Sin conexión",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Error de Conexión",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Verifique la conexión a internet y la disponibilidad de las APIs",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Reintentar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Reintentar Conexión")
        }
    }
}

@Composable
fun DashboardContent(
    dashboardState: com.example.eduoptimaolapii.ui.viewmodels.DashboardState,
    onNavigateToEstudiantes: () -> Unit,
    onNavigateToMatriculas: () -> Unit,
    onNavigateToNotas: () -> Unit,
    onNavigateToMongoDB: () -> Unit,
    onNavigateToOLAP: () -> Unit,
    onLogout: () -> Unit,
    onRefresh: () -> Unit,
    isRefreshing: Boolean
) {
    val dashboardResumen = dashboardState.dashboardResumen ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // HEADER CON ESTADÍSTICAS PRINCIPALES
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📊 Resumen General",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Datos en tiempo real - OLAP + MongoDB",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(
                        onClick = onRefresh,
                        enabled = !isRefreshing
                    ) {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Actualizar")
                        }
                    }
                }
            }
        }

        // MENÚ DE NAVEGACIÓN RÁPIDA
        item {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "🚀 Navegación Rápida",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Primera fila del menú
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NavigationMenuItem(
                            title = "👥 Estudiantes",
                            subtitle = "Gestión",
                            onClick = onNavigateToEstudiantes,
                            modifier = Modifier.weight(1f)
                        )
                        NavigationMenuItem(
                            title = "📝 Matrículas",
                            subtitle = "Procesos",
                            onClick = onNavigateToMatriculas,
                            modifier = Modifier.weight(1f)
                        )
                        NavigationMenuItem(
                            title = "📊 Notas",
                            subtitle = "Académico",
                            onClick = onNavigateToNotas,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Segunda fila del menú
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NavigationMenuItem(
                            title = "🗄️ MongoDB",
                            subtitle = "Base de datos",
                            onClick = onNavigateToMongoDB,
                            modifier = Modifier.weight(1f)
                        )
                        NavigationMenuItem(
                            title = "📈 OLAP",
                            subtitle = "Analíticas",
                            onClick = onNavigateToOLAP,
                            modifier = Modifier.weight(1f)
                        )
                        NavigationMenuItem(
                            title = "🚪 Salir",
                            subtitle = "Cerrar sesión",
                            onClick = onLogout,
                            modifier = Modifier.weight(1f),
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // TARJETAS DE ESTADÍSTICAS PRINCIPALES
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Columna izquierda
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "👥 Total Estudiantes",
                        value = dashboardResumen.totalEstudiantes.toString(),
                        subtitle = "En sistema",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.People,
                        gradientColors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                    )
                    DashboardCard(
                        title = "⭐ Promedio General",
                        value = String.format("%.1f", dashboardResumen.promedioGeneral),
                        subtitle = "Rendimiento académico",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.TrendingUp,
                        gradientColors = listOf(Color(0xFF43e97b), Color(0xFF38f9d7))
                    )
                }

                // Columna derecha
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "📝 Matrículas",
                        value = dashboardResumen.totalMatriculas.toString(),
                        subtitle = "Total registradas",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.School,
                        gradientColors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
                    )
                    DashboardCard(
                        title = "📅 Eventos",
                        value = dashboardResumen.eventosProximos.toString(),
                        subtitle = "Próximos eventos",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Outlined.Event,
                        gradientColors = listOf(Color(0xFFfa709a), Color(0xFFfee140))
                    )
                }
            }
        }

        // SECCIÓN DE GRÁFICAS
        if (dashboardState.promedioPorTrimestre.isNotEmpty() ||
            dashboardState.promedioPorMunicipio.isNotEmpty() ||
            dashboardState.promedioPorGrado.isNotEmpty()) {

            item {
                Text(
                    text = "📈 Análisis Visual",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // GRÁFICA DE PROMEDIO POR TRIMESTRE
            if (dashboardState.promedioPorTrimestre.isNotEmpty()) {
                item {
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📊 Promedio por Trimestre",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    Icons.Default.BarChart,
                                    contentDescription = "Gráfica de barras",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfessionalBarChart(
                                data = dashboardState.promedioPorTrimestre,
                                title = "Evolución Trimestral"
                            )
                        }
                    }
                }
            }

            // GRÁFICAS EN FILA
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Gráfica de Promedio por Municipio
                    if (dashboardState.promedioPorMunicipio.isNotEmpty()) {
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.People,
                                        contentDescription = "Distribución",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🗺️ Promedio por Municipio",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                ProfessionalLineChart(
                                    data = dashboardState.promedioPorMunicipio,
                                    title = "Distribución Municipal"
                                )
                            }
                        }
                    }

                    // Gráfica de Promedio por Grado
                    if (dashboardState.promedioPorGrado.isNotEmpty()) {
                        Card(
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.TrendingUp,
                                        contentDescription = "Rendimiento",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "🎯 Promedio por Grado",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                ProfessionalBarChart(
                                    data = dashboardState.promedioPorGrado,
                                    title = "Rendimiento por Grado"
                                )
                            }
                        }
                    }
                }
            }
        }

        // EVENTOS PRÓXIMOS
// TARJETAS DE ESTADÍSTICAS PRINCIPALES
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Columna izquierda
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "👥 Total Estudiantes",
                        value = dashboardResumen.totalEstudiantes.toString(),
                        subtitle = "En sistema",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.People,
                        gradientColors = listOf(Color(0xFF667eea), Color(0xFF764ba2))
                    )
                    DashboardCard(
                        title = "⭐ Promedio General",
                        value = String.format("%.1f", dashboardResumen.promedioGeneral),
                        subtitle = "Rendimiento académico",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.TrendingUp,
                        gradientColors = listOf(Color(0xFF43e97b), Color(0xFF38f9d7))
                    )
                }

                // Columna derecha
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DashboardCard(
                        title = "📝 Matrículas",
                        value = dashboardResumen.totalMatriculas.toString(),
                        subtitle = "Total registradas",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Default.School,
                        gradientColors = listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
                    )
                    DashboardCard(
                        title = "📅 Eventos",
                        value = dashboardState.eventosProximosList.size.toString(), // Calculado desde la lista
                        subtitle = "Próximos eventos",
                        modifier = Modifier.fillMaxWidth(),
                        icon = Icons.Outlined.Event,
                        gradientColors = listOf(Color(0xFFfa709a), Color(0xFFfee140))
                    )
                }
            }
        }

        // ESPACIO FINAL
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun NavigationMenuItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .height(90.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
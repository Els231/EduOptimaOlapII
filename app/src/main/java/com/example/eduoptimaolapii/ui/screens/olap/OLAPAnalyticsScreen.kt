// File: app/src/main/java/com/example/eduoptimaolapii/ui/screens/olap/OLAPAnalyticsScreen.kt
package com.example.eduoptimaolapii.ui.screens.olap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalBarChart
import com.example.eduoptimaolapii.ui.components.charts.PieChartComponent
import com.example.eduoptimaolapii.ui.components.charts.ProfessionalLineChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OLAPAnalyticsScreen(
    onBack: () -> Unit,
    onNavigateToAdvanced: () -> Unit,
    onNavigateToQuery: () -> Unit
) {
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
                    // AÑADIR estos IconButtons adicionales:
                    IconButton(onClick = onNavigateToAdvanced) {
                        Icon(Icons.Default.Analytics, contentDescription = "Avanzadas")
                    }
                    IconButton(onClick = onNavigateToQuery) {
                        Icon(Icons.Default.Code, contentDescription = "Consultas")
                    }
                }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    text = "Análisis Cubo OLAP",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Datos dimensionales para análisis educativo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // GRÁFICAS DIMENSIONALES DEL CUBO OLAP
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
                                text = "📈 DimEstudiante - Matrículas",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfessionalBarChart(
                            data = mapOf(
                                "Masculino" to 650f,
                                "Femenino" to 606f
                            ),
                            title = "Estudiantes por Sexo"
                        )
                    }
                }
            }

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
                                text = "🎯 Fact_Nota - Rendimiento",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PieChartComponent(
                            data = mapOf(
                                "Aprobado" to 85f,
                                "Reprobado" to 15f
                            ),
                            title = "Distribución de Notas"
                        )
                    }
                }
            }

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
                                text = "📅 DimTiempo - Evolución",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfessionalLineChart(
                            data = mapOf(
                                "Ene" to 78f, "Feb" to 82f, "Mar" to 85f,
                                "Abr" to 88f, "May" to 90f, "Jun" to 87f
                            ),
                            title = "Rendimiento por Mes"
                        )
                    }
                }
            }

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
                                text = "📊 DimCalificacion - Por Grado",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfessionalBarChart(
                            data = mapOf(
                                "1° Grado" to 75f, "2° Grado" to 80f, "3° Grado" to 85f,
                                "4° Grado" to 88f, "5° Grado" to 90f, "6° Grado" to 87f
                            ),
                            title = "Promedio por Grado"
                        )
                    }
                }
            }
        }
    }
}
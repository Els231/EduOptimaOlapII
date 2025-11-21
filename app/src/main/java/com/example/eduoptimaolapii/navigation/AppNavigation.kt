package com.example.eduoptimaolapii.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eduoptimaolapii.ui.screens.auth.LoginScreen
import com.example.eduoptimaolapii.ui.screens.auth.RegisterScreen
import com.example.eduoptimaolapii.ui.screens.dashboard.DashboardScreen
import com.example.eduoptimaolapii.ui.screens.estudiantes.EstudiantesScreen
import com.example.eduoptimaolapii.ui.screens.matriculas.MatriculasScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBDataScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBAnalyticsScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBGridScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBQueryBuilderScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBRealTimeScreen
import com.example.eduoptimaolapii.ui.screens.mongodb.MongoDBExplorerScreen
import com.example.eduoptimaolapii.ui.screens.notas.NotasScreen
import com.example.eduoptimaolapii.ui.screens.olap.OLAPAnalyticsScreen
import com.example.eduoptimaolapii.ui.screens.olap.OLAPAdvancedAnalyticsScreen
import com.example.eduoptimaolapii.ui.screens.olap.OLAPQueryInterfaceScreen
import com.example.eduoptimaolapii.ui.viewmodels.AuthViewModel

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()

    // Navegación automática basada en autenticación
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Dashboard.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // AUTENTICACIÓN
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // DASHBOARD PRINCIPAL
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToEstudiantes = {
                    navController.navigate(Screen.Estudiantes.route)
                },
                onNavigateToMatriculas = {
                    navController.navigate(Screen.Matriculas.route)
                },
                onNavigateToNotas = {
                    navController.navigate(Screen.Notas.route)
                },
                onNavigateToMongoDB = {
                    navController.navigate(Screen.MongoDB.route)
                },
                onNavigateToOLAP = {
                    navController.navigate(Screen.OLAP.route)
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                }
            )
        }

        // MÓDULO ESTUDIANTES
        composable(Screen.Estudiantes.route) {
            EstudiantesScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.EstudiantesAnalytics.route)
                }
            )
        }

        composable(Screen.EstudiantesAnalytics.route) {
            // Pantalla de analíticas de estudiantes (por implementar)
            EstudiantesScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAnalytics = {}
            )
        }

        // MÓDULO MATRÍCULAS
        composable(Screen.Matriculas.route) {
            MatriculasScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.MatriculasAnalytics.route)
                }
            )
        }

        // MÓDULO MATRÍCULAS - ANALYTICS (FALTANTE - CORREGIDO)
        composable(Screen.MatriculasAnalytics.route) {
            // Pantalla temporal de analytics - puedes crear una pantalla específica después
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Analytics",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "📊 Analytics de Matrículas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Pantalla de analíticas en desarrollo...",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Volver a Matrículas")
                }
            }
        }

        // MÓDULO NOTAS
        composable(Screen.Notas.route) {
            NotasScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.NotasAnalytics.route)
                }
            )
        }

        // MONGODB
        composable(Screen.MongoDB.route) {
            MongoDBDataScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToAnalytics = {
                    navController.navigate(Screen.MongoDBAnalytics.route)
                },
                onNavigateToGrid = {
                    navController.navigate(Screen.MongoDBGrid.route)
                },
                onNavigateToQuery = {
                    navController.navigate(Screen.MongoDBQueryBuilder.route)
                },
                onNavigateToRealTime = {
                    navController.navigate(Screen.MongoDBRealTime.route)
                },
                onNavigateToExplorer = {
                    navController.navigate(Screen.MongoDBExplorer.route)
                }
            )
        }

        composable(Screen.MongoDBAnalytics.route) {
            MongoDBAnalyticsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MongoDBGrid.route) {
            MongoDBGridScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MongoDBQueryBuilder.route) {
            MongoDBQueryBuilderScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MongoDBRealTime.route) {
            MongoDBRealTimeScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.MongoDBExplorer.route) {
            MongoDBExplorerScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // OLAP
        composable(Screen.OLAP.route) {
            OLAPAnalyticsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAdvanced = {
                    navController.navigate(Screen.OLAPAdvanced.route)
                },
                onNavigateToQuery = {
                    navController.navigate(Screen.OLAPQuery.route)
                }
            )
        }

        composable(Screen.OLAPAdvanced.route) {
            OLAPAdvancedAnalyticsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.OLAPQuery.route) {
            OLAPQueryInterfaceScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
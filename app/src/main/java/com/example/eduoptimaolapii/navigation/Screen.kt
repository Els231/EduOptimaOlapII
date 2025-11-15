package com.example.eduoptimaolapii.navigation

sealed class Screen(val route: String) {
    // Autenticación
    object Login : Screen("login")
    object Register : Screen("register")

    // Dashboard Principal
    object Dashboard : Screen("dashboard")

    // Módulo Estudiantes
    object Estudiantes : Screen("estudiantes")
    object EstudiantesAnalytics : Screen("estudiantes/analytics")

    // Módulo Matrículas
    object Matriculas : Screen("matriculas")
    object MatriculasAnalytics : Screen("matriculas/analytics")

    // Módulo Notas
    object Notas : Screen("notas")
    object NotasAnalytics : Screen("notas/analytics")

    // MongoDB
    object MongoDB : Screen("mongodb")
    object MongoDBAnalytics : Screen("mongodb/analytics")
    object MongoDBGrid : Screen("mongodb/grid")
    object MongoDBQueryBuilder : Screen("mongodb/query")
    object MongoDBRealTime : Screen("mongodb/realtime")
    object MongoDBExplorer : Screen("mongodb/explorer")

    // OLAP
    object OLAP : Screen("olap")
    object OLAPAdvanced : Screen("olap/advanced")
    object OLAPQuery : Screen("olap/query")

    // Helper functions para navegación anidada
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
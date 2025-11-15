package com.example.eduoptimaolapii.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope,
    currentRoute: String,
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    // Header
                    Text(
                        text = "EduOptima OLAP",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Navigation Items
                    NavigationDrawerItem(
                        label = { Text("Dashboard") },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                        selected = currentRoute == "dashboard",
                        onClick = {
                            onItemSelected("dashboard")
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Estudiantes") },
                        icon = { Icon(Icons.Default.School, contentDescription = null) },
                        selected = currentRoute == "estudiantes",
                        onClick = {
                            onItemSelected("estudiantes")
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Matrículas") },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                        selected = currentRoute == "matriculas",
                        onClick = {
                            onItemSelected("matriculas")
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Notas") },
                        icon = { Icon(Icons.Default.Grade, contentDescription = null) },
                        selected = currentRoute == "notas",
                        onClick = {
                            onItemSelected("notas")
                            coroutineScope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    // Logout at bottom
                    NavigationDrawerItem(
                        label = { Text("Cerrar Sesión") },
                        icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            onLogout()
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = content
    )
}
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eduoptimaolapii.ui.components.ErrorState
import com.example.eduoptimaolapii.ui.viewmodels.EstudianteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MongoDBExplorerScreen(
    onBack: () -> Unit
) {
    val estudianteViewModel: EstudianteViewModel = hiltViewModel()
    val estudianteState by estudianteViewModel.estudianteState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCollection by remember { mutableStateOf("estudiantes") }
    var showCollectionMenu by remember { mutableStateOf(false) }
    var selectedDocument by remember { mutableStateOf<com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo?>(null) }

    LaunchedEffect(Unit) {
        estudianteViewModel.loadEstudiantesData()
    }

    LaunchedEffect(searchQuery) {
        estudianteViewModel.searchEstudiantes(searchQuery)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "🔍 Explorador MongoDB",
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
                        onClick = { estudianteViewModel.refreshData() }
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
                    LoadingExplorer()
                }
                estudianteState.error != null -> {
                    ErrorState(
                        error = estudianteState.error!!,
                        onRetry = { estudianteViewModel.refreshData() }
                    )
                }
                selectedDocument != null -> {
                    DocumentDetailView(
                        document = selectedDocument!!,
                        onBack = { selectedDocument = null }
                    )
                }
                else -> {
                    ExplorerContent(
                        estudianteState = estudianteState,
                        searchQuery = searchQuery,
                        selectedCollection = selectedCollection,
                        showCollectionMenu = showCollectionMenu,
                        onSearchQueryChange = { searchQuery = it },
                        onCollectionChange = { selectedCollection = it },
                        onShowCollectionMenuChange = { showCollectionMenu = it },
                        onDocumentSelect = { selectedDocument = it }
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingExplorer() {
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
            "Cargando explorador de documentos...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ExplorerContent(
    estudianteState: com.example.eduoptimaolapii.ui.viewmodels.EstudianteState,
    searchQuery: String,
    selectedCollection: String,
    showCollectionMenu: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onCollectionChange: (String) -> Unit,
    onShowCollectionMenuChange: (Boolean) -> Unit,
    onDocumentSelect: (com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // BARRA DE HERRAMIENTAS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // SELECTOR DE COLECCIÓN
            ExposedDropdownMenuBox(
                expanded = showCollectionMenu,
                onExpandedChange = onShowCollectionMenuChange,
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = selectedCollection,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Colección") },
                    readOnly = true,
                    leadingIcon = {
                        Icon(Icons.Default.Folder, contentDescription = "Colección")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCollectionMenu)
                    }
                )
                ExposedDropdownMenu(
                    expanded = showCollectionMenu,
                    onDismissRequest = { onShowCollectionMenuChange(false) }
                ) {
                    DropdownMenuItem(
                        text = { Text("estudiantes") },
                        onClick = {
                            onCollectionChange("estudiantes")
                            onShowCollectionMenuChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("matriculas") },
                        onClick = {
                            onCollectionChange("matriculas")
                            onShowCollectionMenuChange(false)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("notas") },
                        onClick = {
                            onCollectionChange("notas")
                            onShowCollectionMenuChange(false)
                        }
                    )
                }
            }

            IconButton(
                onClick = { /* Acción de filtro */ }
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BARRA DE BÚSQUEDA
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Buscar documentos...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            singleLine = true
        )

        // ENCABEZADO DE RESULTADOS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📄 Documentos de $selectedCollection",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${estudianteState.estudiantesFiltrados.size} encontrados",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // LISTA DE DOCUMENTOS
        if (estudianteState.estudiantesFiltrados.isEmpty()) {
            EmptyExplorerState(searchQuery.isNotEmpty())
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(estudianteState.estudiantesFiltrados) { estudiante ->
                    DocumentCard(
                        estudiante = estudiante,
                        onViewDetails = { onDocumentSelect(estudiante) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyExplorerState(isSearching: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Explore,
            contentDescription = "Sin documentos",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isSearching) "No se encontraron documentos" else "No hay documentos en esta colección",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DocumentCard(
    estudiante: com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo,
    onViewDetails: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        onClick = onViewDetails
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // HEADER DEL DOCUMENTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${estudiante.Nombres} ${estudiante.Apellidos}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "ID: ${estudiante._id?.take(8) ?: "N/A"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onViewDetails,
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "Ver detalles",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text("Ver")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // DETALLES DEL DOCUMENTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DocumentField(
                    label = "Código",
                    value = estudiante.CodigoEstudiante,
                    modifier = Modifier.weight(1f)
                )
                DocumentField(
                    label = "Sexo",
                    value = estudiante.Sexo,
                    modifier = Modifier.weight(1f)
                )
                DocumentField(
                    label = "Nacimiento",
                    value = estudiante.FechaNacimiento,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ESTADO DEL DOCUMENTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado: ${estudiante.Estado}",
                    style = MaterialTheme.typography.bodySmall,
                    color = when (estudiante.Estado) {
                        "Activo" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
                        "Inactivo" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Text(
                    text = "Colección: estudiantes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DocumentField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DocumentDetailView(
    document: com.example.eduoptimaolapii.data.model.mongodb.EstudianteMongo,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "📋 Detalles del Documento",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "📄 Documento Completo - MongoDB",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Vista detallada de todos los campos del documento",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // INFORMACIÓN PRINCIPAL
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
                            text = "👤 Información Principal",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        DetailField("ID del Documento", document._id ?: "N/A")
                        DetailField("Código de Estudiante", document.CodigoEstudiante)
                        DetailField("Nombres", document.Nombres)
                        DetailField("Apellidos", document.Apellidos)
                        DetailField("Sexo", document.Sexo)
                        DetailField("Fecha de Nacimiento", document.FechaNacimiento)
                    }
                }
            }

            // INFORMACIÓN DE CONTACTO
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
                            text = "📞 Información de Contacto",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        DetailField("Cédula", document.Cedula ?: "No especificada")
                        DetailField("Dirección", document.Direccion ?: "No especificada")
                        DetailField("Teléfono", document.Telefono ?: "No especificado")
                        DetailField("Email", document.Email ?: "No especificado")
                    }
                }
            }

            // METADATOS DEL DOCUMENTO
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
                            text = "⚙️ Metadatos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        DetailField("Estado", document.Estado)
                        DetailField("Tutor ID", document.TutorIdTutores ?: "No asignado")
                        DetailField("Municipio ID", document.MunicipioIdMunicipio ?: "No asignado")
                        DetailField("Fecha de Creación", document.FechaCreacion ?: "N/A")
                        DetailField("Fecha de Actualización", document.FechaActualizacion ?: "N/A")
                    }
                }
            }

            // ACCIONES
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
                            text = "🔧 Acciones",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { /* Editar documento */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Editar Documento")
                            }
                            Button(
                                onClick = { /* Exportar documento */ },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Exportar JSON")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailField(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}
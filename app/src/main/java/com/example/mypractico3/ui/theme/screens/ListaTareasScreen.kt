package com.example.mypractico3.ui.theme.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen(
    viewModel: TareaViewModel,
    etiquetaViewModel: EtiquetaViewModel,
    onNuevaTarea: () -> Unit,
    onEditarTarea: (TareaConEtiquetas) -> Unit,
    onDetalleTarea: (TareaConEtiquetas) -> Unit,
    onEtiquetas: () -> Unit
) {
    val tareas by viewModel.tareas.collectAsState()
    val filtros by viewModel.filtros.collectAsState()
    val etiquetas by etiquetaViewModel.etiquetas.collectAsState()

    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onEtiquetas) {
                        Icon(Icons.Default.Label, contentDescription = "Etiquetas")
                    }
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros", tint = if (showFilters) MaterialTheme.colorScheme.primary else LocalContentColor.current)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNuevaTarea,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Tarea") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Barra de búsqueda moderna
            OutlinedTextField(
                value = filtros.busqueda,
                onValueChange = { viewModel.cambiarBusqueda(it) },
                placeholder = { Text("Buscar tareas...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                singleLine = true
            )

            if (showFilters) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorSimple(
                                    titulo = "Estado",
                                    valor = filtros.estado,
                                    opciones = listOf("Todas", "Pendientes", "Completadas"),
                                    onSeleccionar = { viewModel.cambiarFiltroEstado(it) }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorSimple(
                                    titulo = "Prioridad",
                                    valor = filtros.prioridad,
                                    opciones = listOf("Todas", "Alta", "Media", "Baja"),
                                    onSeleccionar = { viewModel.cambiarFiltroPrioridad(it) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorSimple(
                                    titulo = "Etiqueta",
                                    valor = filtros.etiqueta,
                                    opciones = listOf("Todas") + etiquetas.map { it.nombre },
                                    onSeleccionar = { viewModel.cambiarFiltroEtiqueta(it) }
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                SelectorSimple(
                                    titulo = "Orden",
                                    valor = filtros.orden,
                                    opciones = listOf("Creación", "Vencimiento", "Prioridad", "Título"),
                                    onSeleccionar = { viewModel.cambiarOrden(it) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (tareas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No hay tareas que mostrar",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el FAB
                ) {
                    items(
                        items = tareas,
                        key = { item -> item.tarea.id }
                    ) { item ->
                        TareaItem(
                            tarea = item.tarea,
                            etiquetas = item.etiquetas,
                            onTareaClick = { onDetalleTarea(item) },
                            onCambiarEstado = { viewModel.cambiarEstado(item.tarea) },
                            onEditar = { onEditarTarea(item) },
                            onEliminar = { viewModel.eliminarTarea(item.tarea) }
                        )
                    }
                }
            }
        }
    }
}

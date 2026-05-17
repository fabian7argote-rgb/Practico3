package com.example.mypractico3.ui.theme.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel



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
    val busqueda by viewModel.busqueda.collectAsState()
    val estado by viewModel.filtroEstado.collectAsState()
    val prioridad by viewModel.filtroPrioridad.collectAsState()
    val orden by viewModel.orden.collectAsState()
    val filtroEtiqueta by viewModel.filtroEtiqueta.collectAsState()
    val etiquetas by etiquetaViewModel.etiquetas.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Lista de tareas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { viewModel.cambiarBusqueda(it) },
            label = { Text("Buscar por título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = onNuevaTarea) {
                Text("Nueva tarea")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onEtiquetas) {
                Text("Etiquetas")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SelectorSimple(
            titulo = "Estado",
            valor = estado,
            opciones = listOf("Todas", "Pendientes", "Completadas"),
            onSeleccionar = { viewModel.cambiarFiltroEstado(it) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        SelectorSimple(
            titulo = "Prioridad",
            valor = prioridad,
            opciones = listOf("Todas", "Alta", "Media", "Baja"),
            onSeleccionar = { viewModel.cambiarFiltroPrioridad(it) }
        )

        Spacer(modifier = Modifier.height(6.dp))

        SelectorSimple(
            titulo = "Etiqueta",
            valor = filtroEtiqueta,
            opciones = listOf("Todas") + etiquetas.map { it.nombre },
            onSeleccionar = {
                viewModel.cambiarFiltroEtiqueta(it)
            }
        )
        Spacer(modifier = Modifier.height(6.dp))

        SelectorSimple(
            titulo = "Orden",
            valor = orden,
            opciones = listOf("Creación", "Vencimiento", "Prioridad", "Título"),
            onSeleccionar = { viewModel.cambiarOrden(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(
                items = tareas,
                key = { item -> item.tarea.id }
            ) { item ->

                Box(
                    modifier = Modifier.clickable {
                        onDetalleTarea(item)
                    }
                ) {
                    TareaItem(
                        tarea = item.tarea,
                        etiquetas = item.etiquetas,
                        onCambiarEstado = {
                            viewModel.cambiarEstado(item.tarea)
                        },
                        onEditar = {
                            onEditarTarea(item)
                        },
                        onEliminar = {
                            viewModel.eliminarTarea(item.tarea)
                        }
                    )
                }
            }
        }
    }
}
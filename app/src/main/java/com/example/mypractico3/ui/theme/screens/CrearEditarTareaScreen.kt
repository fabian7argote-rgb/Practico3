package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel


@Composable
fun CrearEditarTareaScreen(
    tareaEditar: TareaEntity?,
    etiquetasActuales: List<EtiquetaEntity>,
    tareaViewModel: TareaViewModel,
    etiquetaViewModel: EtiquetaViewModel,
    onVolver: () -> Unit
) {
    val etiquetasDisponibles by etiquetaViewModel.etiquetas.collectAsState()

    var titulo by remember { mutableStateOf(tareaEditar?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tareaEditar?.descripcion ?: "") }
    var fecha by remember { mutableStateOf(tareaEditar?.fechaVencimiento ?: "") }
    var prioridad by remember { mutableStateOf(tareaEditar?.prioridad ?: "Media") }
    var errorTitulo by remember { mutableStateOf(false) }

    val seleccionadas = remember {
        mutableStateListOf<Int>().apply {
            addAll(etiquetasActuales.map { it.id })
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = if (tareaEditar == null) "Crear tarea" else "Editar tarea",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                errorTitulo = false
            },
            label = { Text("Título obligatorio") },
            isError = errorTitulo,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorTitulo) {
            Text(
                text = "El título es obligatorio",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fecha,
            onValueChange = { fecha = it },
            label = { Text("Fecha vencimiento Ej: 2026-05-20") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        SelectorSimple(
            titulo = "Prioridad",
            valor = prioridad,
            opciones = listOf("Alta", "Media", "Baja"),
            onSeleccionar = { prioridad = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Etiquetas")

        LazyColumn(
            modifier = Modifier.height(150.dp)
        ) {
            items(
                items = etiquetasDisponibles,
                key = { etiqueta -> etiqueta.id }
            ) { etiqueta ->

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = seleccionadas.contains(etiqueta.id),
                        onCheckedChange = { checked ->
                            if (checked) {
                                seleccionadas.add(etiqueta.id)
                            } else {
                                seleccionadas.remove(etiqueta.id)
                            }
                        }
                    )

                    Text(
                        text = etiqueta.nombre,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(
                onClick = {
                    if (titulo.isBlank()) {
                        errorTitulo = true
                    } else {
                        if (tareaEditar == null) {
                            tareaViewModel.guardarTarea(
                                titulo = titulo,
                                descripcion = descripcion,
                                fechaVencimiento = fecha,
                                prioridad = prioridad,
                                etiquetasSeleccionadas = seleccionadas.toList()
                            )
                        } else {
                            tareaViewModel.actualizarTarea(
                                tarea = tareaEditar,
                                titulo = titulo,
                                descripcion = descripcion,
                                fechaVencimiento = fecha,
                                prioridad = prioridad,
                                etiquetasSeleccionadas = seleccionadas.toList()
                            )
                        }

                        onVolver()
                    }
                }
            ) {
                Text("Guardar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onVolver) {
                Text("Cancelar")
            }
        }
    }
}
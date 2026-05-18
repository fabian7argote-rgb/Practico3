package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val calendar = Calendar.getInstance().apply { timeInMillis = it }
                        fecha = "${calendar.get(Calendar.YEAR)}-${String.format("%02d", calendar.get(Calendar.MONTH) + 1)}-${String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))}"
                    }
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (tareaEditar == null) "Nueva Tarea" else "Editar Tarea", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (titulo.isBlank()) {
                        errorTitulo = true
                    } else {
                        if (tareaEditar == null) {
                            tareaViewModel.guardarTarea(titulo, descripcion, fecha, prioridad, seleccionadas.toList())
                        } else {
                            tareaViewModel.actualizarTarea(tareaEditar, titulo, descripcion, fecha, prioridad, seleccionadas.toList())
                        }
                        onVolver()
                    }
                },
                icon = { Icon(Icons.Default.Check, null) },
                text = { Text("Guardar") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it; errorTitulo = false },
                label = { Text("Título (Obligatorio)") },
                isError = errorTitulo,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha de vencimiento") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, null)
                    }
                }
            )

            // Selector de Prioridad Optimizado
            Text("Prioridad", style = MaterialTheme.typography.titleSmall)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Baja", "Media", "Alta").forEach { p ->
                    FilterChip(
                        selected = prioridad == p,
                        onClick = { prioridad = p },
                        label = { Text(p) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Gestión de Etiquetas Optimizada
            Text("Etiquetas", style = MaterialTheme.typography.titleSmall)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                etiquetasDisponibles.forEach { etiqueta ->
                    FilterChip(
                        selected = seleccionadas.contains(etiqueta.id),
                        onClick = {
                            if (seleccionadas.contains(etiqueta.id)) seleccionadas.remove(etiqueta.id)
                            else seleccionadas.add(etiqueta.id)
                        },
                        label = { Text(etiqueta.nombre) }
                    )
                }
            }
        }
    }
}

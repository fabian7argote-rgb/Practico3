package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EtiquetasScreen(
    viewModel: EtiquetaViewModel,
    onVolver: () -> Unit
) {
    val etiquetas by viewModel.etiquetas.collectAsState()
    var nombre by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestionar Etiquetas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input para nueva etiqueta
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la etiqueta") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (nombre.isNotBlank()) {
                                viewModel.crearEtiqueta(nombre)
                                nombre = ""
                            }
                        },
                        enabled = nombre.isNotBlank()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir")
                    }
                },
                singleLine = true
            )

            HorizontalDivider()

            // Lista de etiquetas
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(
                    items = etiquetas,
                    key = { it.id }
                ) { etiqueta ->
                    ListItem(
                        headlineContent = { Text(etiqueta.nombre) },
                        leadingContent = { Icon(Icons.Default.Label, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = {
                            IconButton(onClick = { viewModel.eliminarEtiqueta(etiqueta) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFEF5350))
                            }
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}

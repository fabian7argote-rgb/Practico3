package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel


@Composable
fun EtiquetasScreen(
    viewModel: EtiquetaViewModel,
    onVolver: () -> Unit
) {
    val etiquetas by viewModel.etiquetas.collectAsState()
    var nombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Gestión de etiquetas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nueva etiqueta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.crearEtiqueta(nombre)
                nombre = ""
            }
        ) {
            Text("Crear etiqueta")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(
                items = etiquetas,
                key = { etiqueta -> etiqueta.id }
            ) { etiqueta ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = etiqueta.nombre,
                        modifier = Modifier.padding(8.dp)
                    )

                    TextButton(
                        onClick = {
                            viewModel.eliminarEtiqueta(etiqueta)
                        }
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.utils.formatearFecha


@Composable
fun TareaItem(
    tarea: TareaEntity,
    etiquetas: List<EtiquetaEntity>,
    onCambiarEstado: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val colorPrioridad = when (tarea.prioridad) {
        "Alta" -> Color(0xFFFFCDD2)
        "Media" -> Color(0xFFFFF9C4)
        else -> Color(0xFFC8E6C9)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(colorPrioridad)
                .padding(12.dp)
        ) {
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            if (tarea.descripcion.isNotBlank()) {
                Text(text = tarea.descripcion)
            }

            Text(text = "Prioridad: ${tarea.prioridad}")

            Text(
                text = "Vence: ${
                    if (tarea.fechaVencimiento.isBlank()) "Sin fecha" else tarea.fechaVencimiento
                }"
            )
            Text("Creada: ${formatearFecha(tarea.fechaCreacion)}")

            Text(
                text = if (tarea.completada) "Estado: Completada" else "Estado: Pendiente"
            )

            if (etiquetas.isNotEmpty()) {
                Text(
                    text = "Etiquetas: ${etiquetas.joinToString { it.nombre }}"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = onCambiarEstado) {
                    Text(if (tarea.completada) "Pendiente" else "Completar")
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(onClick = onEditar) {
                    Text("Editar")
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = onEliminar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}
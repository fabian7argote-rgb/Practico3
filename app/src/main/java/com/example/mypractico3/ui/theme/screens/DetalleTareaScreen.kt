package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.utils.formatearFecha


@Composable
fun DetalleTareaScreen(
    tareaConEtiquetas: TareaConEtiquetas,
    onVolver: () -> Unit
) {
    val tarea = tareaConEtiquetas.tarea

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Detalle de tarea",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Título: ${tarea.titulo}")
        Text("Descripción: ${tarea.descripcion}")
        Text("Prioridad: ${tarea.prioridad}")
        Text("Fecha de vencimiento: ${tarea.fechaVencimiento}")
        Text("Estado: ${if (tarea.completada) "Completada" else "Pendiente"}")
        Text("Fecha de creación: ${formatearFecha(tarea.fechaCreacion)}")

        Spacer(modifier = Modifier.height(12.dp))

        Text("Etiquetas:")

        tareaConEtiquetas.etiquetas.forEach { etiqueta ->
            Text("- ${etiqueta.nombre}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}
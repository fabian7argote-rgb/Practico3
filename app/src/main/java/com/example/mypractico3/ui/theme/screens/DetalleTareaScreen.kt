package com.example.mypractico3.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.utils.PriorityUtils
import com.example.mypractico3.utils.formatearFecha


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetalleTareaScreen(
    tareaConEtiquetas: TareaConEtiquetas,
    onVolver: () -> Unit
) {
    val tarea = tareaConEtiquetas.tarea
    val colorPrioridad = PriorityUtils.getColor(tarea.prioridad)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Card Principal
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título y Estado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tarea.titulo,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        StatusBadge(tarea.completada)
                    }

                    if (tarea.descripcion.isNotBlank()) {
                        InfoSection(Icons.Default.Description, "Descripción", tarea.descripcion)
                    }

                    HorizontalDivider(thickness = 0.5.dp)

                    Row(modifier = Modifier.fillMaxWidth()) {
                        InfoItem(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.PriorityHigh,
                            label = "Prioridad",
                            content = tarea.prioridad,
                            contentColor = colorPrioridad
                        )
                        InfoItem(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Event,
                            label = "Vencimiento",
                            content = tarea.fechaVencimiento.ifBlank { "Sin fecha" }
                        )
                    }

                    InfoItem(
                        icon = Icons.Default.Schedule,
                        label = "Creada el",
                        content = formatearFecha(tarea.fechaCreacion)
                    )
                }
            }

            // Sección de Etiquetas
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Etiquetas",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )

                if (tareaConEtiquetas.etiquetas.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tareaConEtiquetas.etiquetas.forEach { etiqueta ->
                            AssistChip(
                                onClick = { },
                                label = { Text(etiqueta.nombre) },
                                leadingIcon = { Icon(Icons.Default.Tag, null, modifier = Modifier.size(16.dp)) },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No hay etiquetas asignadas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(completada: Boolean) {
    val color = if (completada) Color(0xFF2E7D32) else Color(0xFFC62828)
    val bgColor = if (completada) Color(0xFFC8E6C9) else Color(0xFFFFEBEE)

    Surface(color = bgColor, shape = RoundedCornerShape(12.dp)) {
        Text(
            text = if (completada) "Completada" else "Pendiente",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun InfoSection(icon: ImageVector, label: String, content: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun InfoItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    content: String,
    contentColor: Color = Color.Unspecified
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        }
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = if (contentColor != Color.Unspecified) contentColor else Color.Black
        )
    }
}

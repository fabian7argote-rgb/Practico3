package com.example.mypractico3.ui.theme.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.utils.PriorityUtils



@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TareaItem(
    tarea: TareaEntity,
    etiquetas: List<EtiquetaEntity>,
    onTareaClick: () -> Unit,
    onCambiarEstado: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (tarea.completada) Color(0xFFF5F5F5) else Color.White,
        label = "bgColor"
    )

    val colorPrioridad = PriorityUtils.getColor(tarea.prioridad)

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .animateContentSize(),
        onClick = onTareaClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
            // Barra lateral de prioridad
            Box(modifier = Modifier.fillMaxHeight().width(5.dp).background(colorPrioridad))

            Column(
                modifier = Modifier.padding(12.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tarea.titulo,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            textDecoration = if (tarea.completada) TextDecoration.LineThrough else null,
                            color = if (tarea.completada) Color.Gray else Color.Black
                        )
                        if (tarea.descripcion.isNotBlank()) {
                            Text(
                                text = tarea.descripcion,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Checkbox(
                        checked = tarea.completada,
                        onCheckedChange = { onCambiarEstado() },
                        colors = CheckboxDefaults.colors(checkedColor = colorPrioridad)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.CalendarMonth, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Text(
                            text = tarea.fechaVencimiento.ifBlank { "Sin fecha" },
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                    PriorityBadge(tarea.prioridad, colorPrioridad)
                }

                if (etiquetas.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        etiquetas.forEach { TagChip(it.nombre) }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxHeight().padding(end = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                ActionButton(Icons.Default.Edit, Color.Gray, onEditar)
                ActionButton(Icons.Default.Delete, Color(0xFFEF5350), onEliminar)
            }
        }
    }
}

@Composable
private fun PriorityBadge(text: String, color: Color) {
    Surface(color = color, shape = RoundedCornerShape(8.dp)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TagChip(text: String) {
    Surface(color = Color(0xFFECEFF1), shape = RoundedCornerShape(4.dp)) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun ActionButton(icon: ImageVector, tint: Color, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.size(36.dp)) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(18.dp))
    }
}
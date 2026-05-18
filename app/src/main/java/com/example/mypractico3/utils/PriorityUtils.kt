package com.example.mypractico3.utils

import androidx.compose.ui.graphics.Color

object PriorityUtils {
    fun getColor(prioridad: String): Color = when (prioridad) {
        "Alta" -> Color(0xFFE57373)
        "Media" -> Color(0xFFFFB74D)
        else -> Color(0xFF81C784)
    }

    val opciones = listOf("Alta", "Media", "Baja")
}

package com.example.mypractico3.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String = "",
    val fechaVencimiento: String = "",
    val prioridad: String = "Media",
    val completada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)
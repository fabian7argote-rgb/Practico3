package com.example.mypractico3.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etiquetas")
data class EtiquetaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String
)
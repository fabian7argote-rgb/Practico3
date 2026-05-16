package com.example.mypractico3.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "tarea_etiqueta",
    primaryKeys = ["tareaId", "etiquetaId"],
    foreignKeys = [
        ForeignKey(
            entity = TareaEntity::class,
            parentColumns = ["id"],
            childColumns = ["tareaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EtiquetaEntity::class,
            parentColumns = ["id"],
            childColumns = ["etiquetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("tareaId"),
        Index("etiquetaId")
    ]
)
data class TareaEtiquetaEntity(
    val tareaId: Int,
    val etiquetaId: Int
)
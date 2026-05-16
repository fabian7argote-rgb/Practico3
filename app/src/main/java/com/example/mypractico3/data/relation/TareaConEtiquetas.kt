package com.example.mypractico3.data.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.entity.TareaEtiquetaEntity


data class TareaConEtiquetas(
    @Embedded
    val tarea: TareaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TareaEtiquetaEntity::class,
            parentColumn = "tareaId",
            entityColumn = "etiquetaId"
        )
    )
    val etiquetas: List<EtiquetaEntity>
)
package com.example.mypractico3.data.dao

import androidx.room.*
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.relation.TareaConEtiquetas
import kotlinx.coroutines.flow.Flow


@Dao
interface TareaDao {

    @Transaction
    @Query("SELECT * FROM tareas ORDER BY fechaCreacion DESC")
    fun obtenerTareasConEtiquetas(): Flow<List<TareaConEtiquetas>>

    @Insert
    suspend fun insertarTarea(tarea: TareaEntity): Long

    @Update
    suspend fun actualizarTarea(tarea: TareaEntity)

    @Delete
    suspend fun eliminarTarea(tarea: TareaEntity)

    @Query("UPDATE tareas SET completada = :completada WHERE id = :id")
    suspend fun cambiarEstado(id: Int, completada: Boolean)
}
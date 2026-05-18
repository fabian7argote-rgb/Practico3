package com.example.mypractico3.data.dao
import androidx.room.*
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEtiquetaEntity
import kotlinx.coroutines.flow.Flow



@Dao
interface EtiquetaDao {

    @Query("SELECT * FROM etiquetas ORDER BY nombre ASC")
    fun obtenerEtiquetas(): Flow<List<EtiquetaEntity>>

    @Insert
    suspend fun insertarEtiqueta(etiqueta: EtiquetaEntity)

    @Delete
    suspend fun eliminarEtiqueta(etiqueta: EtiquetaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun asociarEtiquetas(relaciones: List<TareaEtiquetaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun asociarEtiqueta(relacion: TareaEtiquetaEntity)

    @Query("DELETE FROM tarea_etiqueta WHERE tareaId = :tareaId")
    suspend fun eliminarRelacionesDeTarea(tareaId: Int)

    @Query("DELETE FROM tarea_etiqueta WHERE etiquetaId = :etiquetaId")
    suspend fun eliminarRelacionesDeEtiqueta(etiquetaId: Int)
}
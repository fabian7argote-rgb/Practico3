package com.example.mypractico3.data.repository

import com.example.mypractico3.data.dao.EtiquetaDao
import com.example.mypractico3.data.dao.TareaDao
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.entity.TareaEtiquetaEntity


class TareaRepository(
    private val tareaDao: TareaDao,
    private val etiquetaDao: EtiquetaDao
) {
    val tareas = tareaDao.obtenerTareasConEtiquetas()
    val etiquetas = etiquetaDao.obtenerEtiquetas()

    suspend fun insertarTarea(tarea: TareaEntity, etiquetasSeleccionadas: List<Int>) {
        val tareaId = tareaDao.insertarTarea(tarea).toInt()
        asociarEtiquetas(tareaId, etiquetasSeleccionadas)
    }

    suspend fun actualizarTarea(tarea: TareaEntity, etiquetasSeleccionadas: List<Int>) {
        tareaDao.actualizarTarea(tarea)
        etiquetaDao.eliminarRelacionesDeTarea(tarea.id)
        asociarEtiquetas(tarea.id, etiquetasSeleccionadas)
    }

    private suspend fun asociarEtiquetas(tareaId: Int, etiquetasIds: List<Int>) {
        if (etiquetasIds.isNotEmpty()) {
            val relaciones = etiquetasIds.map { TareaEtiquetaEntity(tareaId, it) }
            etiquetaDao.asociarEtiquetas(relaciones)
        }
    }

    suspend fun eliminarTarea(tarea: TareaEntity) {
        // La eliminación de relaciones es automática por CASCADE en la base de datos,
        // pero lo mantenemos si queremos ser explícitos o si no confiamos en el esquema.
        // Dado que TareaEtiquetaEntity tiene onDelete = CASCADE, no es estrictamente necesario.
        tareaDao.eliminarTarea(tarea)
    }

    suspend fun cambiarEstado(id: Int, completada: Boolean) = tareaDao.cambiarEstado(id, completada)

    suspend fun insertarEtiqueta(nombre: String) = etiquetaDao.insertarEtiqueta(EtiquetaEntity(nombre = nombre))

    suspend fun eliminarEtiqueta(etiqueta: EtiquetaEntity) = etiquetaDao.eliminarEtiqueta(etiqueta)
}

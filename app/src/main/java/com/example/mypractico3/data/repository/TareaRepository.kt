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

    suspend fun insertarTarea(
        tarea: TareaEntity,
        etiquetasSeleccionadas: List<Int>
    ) {
        val tareaId = tareaDao.insertarTarea(tarea).toInt()

        etiquetasSeleccionadas.forEach { etiquetaId ->
            etiquetaDao.asociarEtiqueta(
                TareaEtiquetaEntity(
                    tareaId = tareaId,
                    etiquetaId = etiquetaId
                )
            )
        }
    }

    suspend fun actualizarTarea(
        tarea: TareaEntity,
        etiquetasSeleccionadas: List<Int>
    ) {
        tareaDao.actualizarTarea(tarea)

        etiquetaDao.eliminarRelacionesDeTarea(tarea.id)

        etiquetasSeleccionadas.forEach { etiquetaId ->
            etiquetaDao.asociarEtiqueta(
                TareaEtiquetaEntity(
                    tareaId = tarea.id,
                    etiquetaId = etiquetaId
                )
            )
        }
    }

    suspend fun eliminarTarea(tarea: TareaEntity) {
        etiquetaDao.eliminarRelacionesDeTarea(tarea.id)
        tareaDao.eliminarTarea(tarea)
    }

    suspend fun cambiarEstado(
        id: Int,
        completada: Boolean
    ) {
        tareaDao.cambiarEstado(id, completada)
    }

    suspend fun insertarEtiqueta(nombre: String) {
        etiquetaDao.insertarEtiqueta(
            EtiquetaEntity(nombre = nombre)
        )
    }

    suspend fun eliminarEtiqueta(etiqueta: EtiquetaEntity) {
        etiquetaDao.eliminarRelacionesDeEtiqueta(etiqueta.id)
        etiquetaDao.eliminarEtiqueta(etiqueta)
    }
}
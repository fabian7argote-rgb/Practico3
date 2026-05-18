package com.example.mypractico3.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.data.repository.TareaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FiltrosEstado(
    val busqueda: String = "",
    val estado: String = "Todas",
    val prioridad: String = "Todas",
    val etiqueta: String = "Todas",
    val orden: String = "Creación"
)

class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    private val _filtros = MutableStateFlow(FiltrosEstado())
    val filtros = _filtros.asStateFlow()

    val tareas: StateFlow<List<TareaConEtiquetas>> =
        combine(repository.tareas, _filtros) { lista, f ->
            lista.filter { item ->
                (f.busqueda.isBlank() || item.tarea.titulo.contains(f.busqueda, ignoreCase = true)) &&
                        (f.estado == "Todas" || (f.estado == "Completadas" && item.tarea.completada) || (f.estado == "Pendientes" && !item.tarea.completada)) &&
                        (f.prioridad == "Todas" || item.tarea.prioridad == f.prioridad) &&
                        (f.etiqueta == "Todas" || item.etiquetas.any { it.nombre == f.etiqueta })
            }.let { filtered ->
                when (f.orden) {
                    "Título" -> filtered.sortedBy { it.tarea.titulo }
                    "Prioridad" -> filtered.sortedBy { it.tarea.prioridad }
                    "Vencimiento" -> filtered.sortedBy { it.tarea.fechaVencimiento }
                    else -> filtered.sortedByDescending { it.tarea.fechaCreacion }
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarBusqueda(v: String) { _filtros.update { it.copy(busqueda = v) } }
    fun cambiarFiltroEstado(v: String) { _filtros.update { it.copy(estado = v) } }
    fun cambiarFiltroPrioridad(v: String) { _filtros.update { it.copy(prioridad = v) } }
    fun cambiarFiltroEtiqueta(v: String) { _filtros.update { it.copy(etiqueta = v) } }
    fun cambiarOrden(v: String) { _filtros.update { it.copy(orden = v) } }

    fun guardarTarea(titulo: String, descripcion: String, fechaVencimiento: String, prioridad: String, etiquetasSeleccionadas: List<Int>) {
        viewModelScope.launch {
            repository.insertarTarea(
                TareaEntity(titulo = titulo, descripcion = descripcion, fechaVencimiento = fechaVencimiento, prioridad = prioridad),
                etiquetasSeleccionadas
            )
        }
    }

    fun actualizarTarea(tarea: TareaEntity, titulo: String, descripcion: String, fechaVencimiento: String, prioridad: String, etiquetasSeleccionadas: List<Int>) {
        viewModelScope.launch {
            repository.actualizarTarea(
                tarea.copy(titulo = titulo, descripcion = descripcion, fechaVencimiento = fechaVencimiento, prioridad = prioridad),
                etiquetasSeleccionadas
            )
        }
    }

    fun eliminarTarea(tarea: TareaEntity) = viewModelScope.launch { repository.eliminarTarea(tarea) }
    fun cambiarEstado(tarea: TareaEntity) = viewModelScope.launch { repository.cambiarEstado(tarea.id, !tarea.completada) }
}
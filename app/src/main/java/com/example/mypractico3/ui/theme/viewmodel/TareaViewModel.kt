package com.example.mypractico3.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.data.repository.TareaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TareaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtroEstado = MutableStateFlow("Todas")
    val filtroEstado: StateFlow<String> = _filtroEstado.asStateFlow()

    private val _filtroPrioridad = MutableStateFlow("Todas")
    val filtroPrioridad: StateFlow<String> = _filtroPrioridad.asStateFlow()

    private val _filtroEtiqueta = MutableStateFlow("Todas")
    val filtroEtiqueta: StateFlow<String> = _filtroEtiqueta.asStateFlow()

    private val _orden = MutableStateFlow("Creación")
    val orden: StateFlow<String> = _orden.asStateFlow()

    val tareas: StateFlow<List<TareaConEtiquetas>> =
        combine(
            repository.tareas,
            _busqueda,
            _filtroEstado,
            _filtroPrioridad,
            _filtroEtiqueta,
            _orden
        ) { valores: Array<Any?> ->

            val lista = valores[0] as List<TareaConEtiquetas>
            val busqueda = valores[1] as String
            val estado = valores[2] as String
            val prioridad = valores[3] as String
            val etiqueta = valores[4] as String
            val orden = valores[5] as String

            var resultado: List<TareaConEtiquetas> = lista

            if (busqueda.isNotBlank()) {
                resultado = resultado.filter { item ->
                    item.tarea.titulo.contains(busqueda, ignoreCase = true)
                }
            }

            resultado = when (estado) {
                "Pendientes" -> resultado.filter { item ->
                    !item.tarea.completada
                }

                "Completadas" -> resultado.filter { item ->
                    item.tarea.completada
                }

                else -> resultado
            }

            resultado = when (prioridad) {
                "Alta" -> resultado.filter { item ->
                    item.tarea.prioridad == "Alta"
                }

                "Media" -> resultado.filter { item ->
                    item.tarea.prioridad == "Media"
                }

                "Baja" -> resultado.filter { item ->
                    item.tarea.prioridad == "Baja"
                }

                else -> resultado
            }

            if (etiqueta != "Todas") {
                resultado = resultado.filter { item ->
                    item.etiquetas.any { etiquetaItem ->
                        etiquetaItem.nombre == etiqueta
                    }
                }
            }

            resultado = when (orden) {
                "Título" -> resultado.sortedBy { item ->
                    item.tarea.titulo
                }

                "Prioridad" -> resultado.sortedBy { item ->
                    item.tarea.prioridad
                }

                "Vencimiento" -> resultado.sortedBy { item ->
                    item.tarea.fechaVencimiento
                }

                else -> resultado.sortedByDescending { item ->
                    item.tarea.fechaCreacion
                }
            }

            resultado
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun cambiarBusqueda(valor: String) {
        _busqueda.value = valor
    }

    fun cambiarFiltroEstado(valor: String) {
        _filtroEstado.value = valor
    }

    fun cambiarFiltroPrioridad(valor: String) {
        _filtroPrioridad.value = valor
    }

    fun cambiarFiltroEtiqueta(valor: String) {
        _filtroEtiqueta.value = valor
    }

    fun cambiarOrden(valor: String) {
        _orden.value = valor
    }

    fun guardarTarea(
        titulo: String,
        descripcion: String,
        fechaVencimiento: String,
        prioridad: String,
        etiquetasSeleccionadas: List<Int>
    ) {
        viewModelScope.launch {
            repository.insertarTarea(
                tarea = TareaEntity(
                    titulo = titulo,
                    descripcion = descripcion,
                    fechaVencimiento = fechaVencimiento,
                    prioridad = prioridad
                ),
                etiquetasSeleccionadas = etiquetasSeleccionadas
            )
        }
    }

    fun actualizarTarea(
        tarea: TareaEntity,
        titulo: String,
        descripcion: String,
        fechaVencimiento: String,
        prioridad: String,
        etiquetasSeleccionadas: List<Int>
    ) {
        viewModelScope.launch {
            repository.actualizarTarea(
                tarea = tarea.copy(
                    titulo = titulo,
                    descripcion = descripcion,
                    fechaVencimiento = fechaVencimiento,
                    prioridad = prioridad
                ),
                etiquetasSeleccionadas = etiquetasSeleccionadas
            )
        }
    }

    fun eliminarTarea(tarea: TareaEntity) {
        viewModelScope.launch {
            repository.eliminarTarea(tarea)
        }
    }

    fun cambiarEstado(tarea: TareaEntity) {
        viewModelScope.launch {
            repository.cambiarEstado(
                id = tarea.id,
                completada = !tarea.completada
            )
        }
    }
}
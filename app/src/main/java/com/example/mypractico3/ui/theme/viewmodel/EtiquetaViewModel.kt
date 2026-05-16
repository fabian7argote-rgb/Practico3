package com.example.mypractico3.ui.theme.viewmodel

import androidx.lifecycle.ViewModel

import com.example.mypractico3.data.repository.TareaRepository
import androidx.lifecycle.viewModelScope
import com.example.mypractico3.data.entity.EtiquetaEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EtiquetaViewModel(
    private val repository: TareaRepository
) : ViewModel() {

    val etiquetas = repository.etiquetas.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun crearEtiqueta(nombre: String) {
        viewModelScope.launch {
            if (nombre.isNotBlank()) {
                repository.insertarEtiqueta(nombre.trim())
            }
        }
    }

    fun eliminarEtiqueta(etiqueta: EtiquetaEntity) {
        viewModelScope.launch {
            repository.eliminarEtiqueta(etiqueta)
        }
    }
}
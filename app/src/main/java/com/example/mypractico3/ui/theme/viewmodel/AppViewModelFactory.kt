package com.example.mypractico3.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mypractico3.data.repository.TareaRepository


class AppViewModelFactory(
    private val repository: TareaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TareaViewModel::class.java) -> TareaViewModel(repository) as T
            modelClass.isAssignableFrom(EtiquetaViewModel::class.java) -> EtiquetaViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel desconocido")
        }
    }
}
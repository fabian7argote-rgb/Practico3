package com.example.mypractico3.ui.theme.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mypractico3.data.repository.TareaRepository


class AppViewModelFactory(
    private val repository: TareaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(TareaViewModel::class.java)) {
            return TareaViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(EtiquetaViewModel::class.java)) {
            return EtiquetaViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
package com.example.mypractico3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypractico3.data.AppDatabase
import com.example.mypractico3.data.relation.TareaConEtiquetas
import com.example.mypractico3.data.repository.TareaRepository
import com.example.mypractico3.ui.theme.screens.CrearEditarTareaScreen
import com.example.mypractico3.ui.theme.screens.DetalleTareaScreen
import com.example.mypractico3.ui.theme.screens.EtiquetasScreen
import com.example.mypractico3.ui.theme.screens.ListaTareasScreen
import com.example.mypractico3.ui.theme.viewmodel.AppViewModelFactory
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel




class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.obtenerDatabase(this)

        val repository = TareaRepository(
            tareaDao = database.tareaDao(),
            etiquetaDao = database.etiquetaDao()
        )

        val factory = AppViewModelFactory(repository)

        setContent {
            val tareaViewModel: TareaViewModel = viewModel(factory = factory)
            val etiquetaViewModel: EtiquetaViewModel = viewModel(factory = factory)

            var pantallaActual by androidx.compose.runtime.remember {
                androidx.compose.runtime.mutableStateOf("lista")
            }

            var tareaSeleccionada by androidx.compose.runtime.remember {
                androidx.compose.runtime.mutableStateOf<TareaConEtiquetas?>(null)
            }

            when (pantallaActual) {
                "lista" -> {
                    ListaTareasScreen(
                        viewModel = tareaViewModel,
                        onNuevaTarea = {
                            tareaSeleccionada = null
                            pantallaActual = "crear"
                        },
                        onEditarTarea = { tarea ->
                            tareaSeleccionada = tarea
                            pantallaActual = "editar"
                        },
                        onDetalleTarea = { tarea ->
                            tareaSeleccionada = tarea
                            pantallaActual = "detalle"
                        },
                        onEtiquetas = {
                            pantallaActual = "etiquetas"
                        }
                    )
                }

                "crear" -> {
                    CrearEditarTareaScreen(
                        tareaEditar = null,
                        etiquetasActuales = emptyList(),
                        tareaViewModel = tareaViewModel,
                        etiquetaViewModel = etiquetaViewModel,
                        onVolver = {
                            pantallaActual = "lista"
                        }
                    )
                }

                "editar" -> {
                    tareaSeleccionada?.let { tarea ->
                        CrearEditarTareaScreen(
                            tareaEditar = tarea.tarea,
                            etiquetasActuales = tarea.etiquetas,
                            tareaViewModel = tareaViewModel,
                            etiquetaViewModel = etiquetaViewModel,
                            onVolver = {
                                pantallaActual = "lista"
                            }
                        )
                    }
                }

                "detalle" -> {
                    tareaSeleccionada?.let { tarea ->
                        DetalleTareaScreen(
                            tareaConEtiquetas = tarea,
                            onVolver = {
                                pantallaActual = "lista"
                            }
                        )
                    }
                }

                "etiquetas" -> {
                    EtiquetasScreen(
                        viewModel = etiquetaViewModel,
                        onVolver = {
                            pantallaActual = "lista"
                        }
                    )
                }
            }
        }
    }
}
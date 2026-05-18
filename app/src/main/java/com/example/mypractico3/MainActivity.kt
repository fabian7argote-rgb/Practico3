package com.example.mypractico3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mypractico3.data.AppDatabase
import com.example.mypractico3.data.repository.TareaRepository
import com.example.mypractico3.ui.theme.screens.*
import com.example.mypractico3.ui.theme.viewmodel.AppViewModelFactory
import com.example.mypractico3.ui.theme.viewmodel.EtiquetaViewModel
import com.example.mypractico3.ui.theme.viewmodel.TareaViewModel




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.obtenerDatabase(this)
        val repository = TareaRepository(database.tareaDao(), database.etiquetaDao())
        val factory = AppViewModelFactory(repository)

        setContent {
            val navController = rememberNavController()
            val tareaVM: TareaViewModel = viewModel(factory = factory)
            val etiquetaVM: EtiquetaViewModel = viewModel(factory = factory)
            val tareas by tareaVM.tareas.collectAsState()

            NavHost(navController = navController, startDestination = "lista") {
                composable("lista") {
                    ListaTareasScreen(
                        viewModel = tareaVM,
                        etiquetaViewModel = etiquetaVM,
                        onNuevaTarea = { navController.navigate("crear") },
                        onEditarTarea = { navController.navigate("editar/${it.tarea.id}") },
                        onDetalleTarea = { navController.navigate("detalle/${it.tarea.id}") },
                        onEtiquetas = { navController.navigate("etiquetas") }
                    )
                }
                composable("crear") {
                    CrearEditarTareaScreen(null, emptyList(), tareaVM, etiquetaVM) { navController.popBackStack() }
                }
                composable("editar/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                    val item = tareas.find { it.tarea.id == id }
                    item?.let {
                        CrearEditarTareaScreen(it.tarea, it.etiquetas, tareaVM, etiquetaVM) { navController.popBackStack() }
                    }
                }
                composable("detalle/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                    val item = tareas.find { it.tarea.id == id }
                    item?.let {
                        DetalleTareaScreen(it) { navController.popBackStack() }
                    }
                }
                composable("etiquetas") {
                    EtiquetasScreen(etiquetaVM) { navController.popBackStack() }
                }
            }
        }
    }
}

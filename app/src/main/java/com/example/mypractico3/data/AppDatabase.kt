package com.example.mypractico3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mypractico3.data.dao.EtiquetaDao
import com.example.mypractico3.data.dao.TareaDao
import com.example.mypractico3.data.entity.EtiquetaEntity
import com.example.mypractico3.data.entity.TareaEntity
import com.example.mypractico3.data.entity.TareaEtiquetaEntity

@Database(
    entities = [
        TareaEntity::class,
        EtiquetaEntity::class,
        TareaEtiquetaEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tareaDao(): TareaDao
    abstract fun etiquetaDao(): EtiquetaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tareas_db"
                ).build()

                INSTANCE = instancia
                instancia
            }
        }
    }
}
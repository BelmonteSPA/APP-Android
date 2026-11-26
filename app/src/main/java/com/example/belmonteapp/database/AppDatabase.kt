package com.example.belmonteapp.database

import android.content.Context
import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.belmonteapp.dao.ConductorDao
import com.example.belmonteapp.dao.ResenaDao
import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.dao.ViajeDao
import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.modelo.Resena
import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.modelo.Viajes


@Database(
    entities = [Viajes::class, Conductor::class, Resena::class, Sesion::class],
    version = 6,
    exportSchema = false
)
@ColumnTypeConverters (Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun viajeDao() : ViajeDao
    abstract fun conductorDao() : ConductorDao
    abstract fun resenaDao() : ResenaDao
    abstract fun sesionDao() : SesionDao
    companion object {
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase (context: Context) : AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "db_belmonte"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
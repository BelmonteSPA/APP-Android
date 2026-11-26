package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.belmonteapp.modelo.Conductor
import kotlinx.coroutines.flow.Flow

@Dao
interface ConductorDao {

    //REPLACE: si el conductor ya existe (mismo id) se actualiza con lo que llega del servidor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun almacenarConductores (conductores : List<Conductor>)

    @Query ("SELECT * FROM conductor ORDER BY nombre ASC")
    fun obtenerConductores () : Flow<List<Conductor>>

    @Query ("SELECT COUNT(*) FROM conductor")
    suspend fun contarConductores () : Int
}
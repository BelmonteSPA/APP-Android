package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.belmonteapp.modelo.Conductor
import kotlinx.coroutines.flow.Flow

@Dao
interface ConductorDao {

    @Insert
    suspend fun almacenarConductores (conductores : List<Conductor>)

    @Query ("SELECT * FROM conductor ORDER BY nombre ASC")
    fun obtenerConductores () : Flow<List<Conductor>>

    @Query ("SELECT COUNT(*) FROM conductor")
    suspend fun contarConductores () : Int
}
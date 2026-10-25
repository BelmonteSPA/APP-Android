package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.belmonteapp.modelo.Viajes
import kotlinx.coroutines.flow.Flow

@Dao
interface ViajeDao {

    @Insert
    suspend fun almacenarViaje (viaje: Viajes)

    @Insert
    suspend fun almacenarViajes (viajes : List<Viajes>)

    @Update
    suspend fun actualizarViaje (viaje : Viajes)

    @Delete
    suspend fun eliminarViaje (viaje : Viajes)

    @Query ("SELECT * FROM viajes ORDER BY fecha ASC, hora ASC")
    fun obtenerViajes () : Flow<List<Viajes>>

    @Query ("SELECT COUNT(*) FROM viajes")
    suspend fun contarViajes () : Int
}
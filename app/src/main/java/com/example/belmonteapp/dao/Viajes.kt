package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface Viajes {

    @Insert
    suspend fun almacenarViaje(viaje: Viajes)

    @Update
    suspend fun actualizarViaje(viaje: Viajes)

    @Delete
    suspend fun eliminarViaje(viaje: Viajes)

    @Query("SELECT * FROM viaje ORDER BY id DESC")
    fun obtenerViaje(): Flow<List<Viajes>>
}
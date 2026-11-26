package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.example.belmonteapp.modelo.Viajes
import kotlinx.coroutines.flow.Flow

@Dao
interface ViajeDao {

    //REPLACE: si el viaje ya existe (mismo id) se reemplaza con lo que llega del servidor
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun almacenarViaje (viaje: Viajes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun almacenarViajes (viajes : List<Viajes>)

    @Update
    suspend fun actualizarViaje (viaje : Viajes)

    @Delete
    suspend fun eliminarViaje (viaje : Viajes)

    @Query ("SELECT * FROM viajes ORDER BY fecha ASC, hora ASC")
    fun obtenerViajes () : Flow<List<Viajes>>

    @Query ("SELECT COUNT(*) FROM viajes")
    suspend fun contarViajes () : Int

    //borra los viajes que ya no vienen del servidor (los demas se actualizan con REPLACE)
    @Query ("DELETE FROM viajes WHERE id NOT IN (:ids)")
    suspend fun borrarViajesExcepto (ids : List<Int>)
}
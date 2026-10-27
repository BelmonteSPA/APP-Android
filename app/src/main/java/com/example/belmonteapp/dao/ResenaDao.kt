package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.belmonteapp.modelo.Resena
import kotlinx.coroutines.flow.Flow

@Dao
interface ResenaDao {
    @Insert
    suspend fun almacenarResena(resena: Resena)

    @Query("SELECT * FROM resena ORDER BY fechaMillis DESC")
    fun obtenerResenas(): Flow<List<Resena>>
}
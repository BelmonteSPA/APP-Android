package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.example.belmonteapp.modelo.Sesion

@Dao
interface SesionDao {

    //REPLACE: si ya habia una sesion (id = 1) se reemplaza por la nueva
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(sesion: Sesion)

    @Query("SELECT * FROM sesion WHERE id = 1")
    suspend fun obtener(): Sesion?

    @Query("DELETE FROM sesion")
    suspend fun borrar()
}
package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.belmonteapp.modelo.Cuenta
import kotlinx.coroutines.flow.Flow

@Dao
interface CuentaDao {
    @Insert
    suspend fun almacenarCuenta(cuenta: Cuenta)

    @Update
    suspend fun actualizarCuenta(cuenta: Cuenta)

    @Delete
    suspend fun eliminarCuenta(cuenta: Cuenta)

    @Query("SELECT * FROM cuenta ORDER BY id DESC")
    fun obtenerCuenta(): Flow<List<Cuenta>>


}
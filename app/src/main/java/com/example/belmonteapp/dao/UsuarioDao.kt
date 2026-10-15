package com.example.belmonteapp.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.belmonteapp.modelo.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Insert
    suspend fun almacenarUsuario(usuario: Usuario)

    @Update
    suspend fun actualizarUsuario(usuario: Usuario)

    @Delete
    suspend fun  eliminarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario ORDEN BY id DESC")
    fun obtenerUsuario(): Flow<List<Usuario>>

}
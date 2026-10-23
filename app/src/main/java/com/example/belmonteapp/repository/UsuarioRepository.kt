package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.UsuarioDao
import com.example.belmonteapp.modelo.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository (
    private val usuarioDao: UsuarioDao


){
    val usuario: Flow<List<Usuario>> = usuarioDao.obtenerUsuario()

    suspend fun almacenarUsuario(usuario: Usuario) {
        usuarioDao.almacenarUsuario(usuario)
    }

    suspend fun eliminarUsuario(usuario: Usuario) {
        usuarioDao.eliminarUsuario(usuario)
    }

    suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizarUsuario(usuario)
    }
    fun usuarioPorId(id: Int): Flow<Usuario?> = usuarioDao.obtenerUsuarioPorId(id)
}
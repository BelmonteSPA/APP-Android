package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.remote.LoginRequest
import com.example.belmonteapp.remote.RetrofitClient

class SesionRepository(
    private val sesionDao: SesionDao
) {
    private val api = RetrofitClient.api

    //Login contra ms-auth (a traves del gateway) y guarda el token en Room.
    //Lanza HttpException si la clave es incorrecta (401) o IOException si el servidor no responde.
    suspend fun login(email: String, password: String): Sesion {
        sesionDao.borrar() //se parte sin sesion: si el login falla, la app queda en modo local

        val respuesta = api.login(LoginRequest(email, password))
        val sesion = Sesion(
            token = respuesta.token,
            email = respuesta.usuario.email,
            rol = respuesta.usuario.rol,
            idReferencia = respuesta.usuario.idReferencia ?: 0
        )
        sesionDao.guardar(sesion)
        return sesion
    }

    //Cerrar sesion: borra el token de Room (la app vuelve a quedar sin servidor)
    suspend fun cerrarSesion() {
        sesionDao.borrar()
    }
}
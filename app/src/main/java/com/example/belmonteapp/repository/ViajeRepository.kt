package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.dao.ViajeDao
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.remote.AsignarRequest
import com.example.belmonteapp.remote.RetrofitClient
import com.example.belmonteapp.remote.aNuevoViajeRequest
import com.example.belmonteapp.remote.aViaje
import kotlinx.coroutines.flow.Flow

//Las pantallas siempre leen de Room. Si hay sesion con el backend, los cambios se envian
//primero a ms-viajes (Retrofit) y lo que responde se guarda en Room.
class ViajeRepository (
    private val viajeDao: ViajeDao,
    private val sesionDao: SesionDao
){
    private val api = RetrofitClient.api

    val viajes : Flow<List<Viajes>> = viajeDao.obtenerViajes()

    suspend fun almacenarViaje (viaje : Viajes) {
        viajeDao.almacenarViaje(viaje)
    }

    suspend fun almacenarViajes (viajes : List<Viajes>) {
        viajeDao.almacenarViajes(viajes)
    }

    suspend fun actualizarViaje(viaje: Viajes) {
        viajeDao.actualizarViaje(viaje)
    }

    suspend fun eliminarViaje(viaje: Viajes) {
        viajeDao.eliminarViaje(viaje)
    }

    suspend fun contarViajes(): Int {
        return viajeDao.contarViajes()
    }

    //---------- Backend ----------

    //true si el usuario inicio sesion contra el backend (hay token en Room)
    suspend fun hayServidor(): Boolean {
        return sesionDao.obtener() != null
    }

    private suspend fun token(): String {
        return "Bearer ${sesionDao.obtener()?.token}"
    }

    //Descarga los viajes segun el rol y deja en Room exactamente esos viajes
    suspend fun sincronizar(rutPasajero: String) {
        val sesion = sesionDao.obtener() ?: return
        val token = "Bearer ${sesion.token}"

        val lista = when (sesion.rol) {
            "CONDUCTOR" -> api.viajesDelConductor(token, sesion.idReferencia)
            "PASAJERO" -> api.viajesDelPasajero(token, sesion.idReferencia)
            else -> api.obtenerViajes(token) //ADMIN
        }

        viajeDao.almacenarViajes(lista.map { it.aViaje(rutPasajero) })
        viajeDao.borrarViajesExcepto(lista.map { it.id })
    }

    //Pasajero: POST /viajes (el backend le asigna el id y queda PENDIENTE)
    suspend fun crearEnServidor(viaje: Viajes) {
        val sesion = sesionDao.obtener() ?: return
        val creado = api.crearViaje(token(), viaje.aNuevoViajeRequest(sesion.idReferencia))
        viajeDao.almacenarViaje(creado.aViaje(viaje.pasajeroRut))
    }

    //Administrador: PUT /viajes/{id}/asignar
    suspend fun asignarEnServidor(viaje: Viajes, idConductor: Int) {
        val actualizado = api.asignarConductor(token(), viaje.id, AsignarRequest(idConductor))
        viajeDao.almacenarViaje(actualizado.aViaje(viaje.pasajeroRut))
    }

    //Conductor: PATCH /viajes/{id}/iniciar
    suspend fun iniciarEnServidor(viaje: Viajes) {
        val actualizado = api.iniciarViaje(token(), viaje.id)
        viajeDao.almacenarViaje(actualizado.aViaje(viaje.pasajeroRut))
    }

    //Conductor: PATCH /viajes/{id}/finalizar
    suspend fun finalizarEnServidor(viaje: Viajes) {
        val actualizado = api.finalizarViaje(token(), viaje.id)
        viajeDao.almacenarViaje(actualizado.aViaje(viaje.pasajeroRut))
    }
}
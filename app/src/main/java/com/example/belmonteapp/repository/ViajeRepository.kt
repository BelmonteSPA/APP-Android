package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.ViajeDao
import com.example.belmonteapp.modelo.Viajes
import kotlinx.coroutines.flow.Flow

class ViajeRepository (
    private val viajeDao: ViajeDao
){
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
}
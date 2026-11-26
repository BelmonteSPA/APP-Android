package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.ConductorDao
import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.remote.RetrofitClient
import com.example.belmonteapp.remote.aConductor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ConductorRepository (
    private val conductorDao: ConductorDao,
    private val sesionDao: SesionDao
){
    private val api = RetrofitClient.api

    val conductores : Flow<List<Conductor>> = conductorDao.obtenerConductores()

    suspend fun almacenarConductores (conductores : List<Conductor>) {
        conductorDao.almacenarConductores(conductores)
    }

    suspend fun contarConductores () : Int {
        return conductorDao.contarConductores()
    }

    //Solo el administrador puede listar conductores (GET /conductores).
    //El backend no guarda RUT, asi que se conserva el que ya estaba en Room.
    suspend fun sincronizar() {
        val sesion = sesionDao.obtener() ?: return
        if (sesion.rol != "ADMIN") return

        val locales = conductorDao.obtenerConductores().first()
        val lista = api.obtenerConductores("Bearer ${sesion.token}")

        conductorDao.almacenarConductores(
            lista.map { dto ->
                val rut = locales.find { it.id == dto.id }?.rut ?: ""
                dto.aConductor(rut)
            }
        )
    }
}
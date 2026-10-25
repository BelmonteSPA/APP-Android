package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.ConductorDao
import com.example.belmonteapp.modelo.Conductor
import kotlinx.coroutines.flow.Flow

class ConductorRepository (
    private val conductorDao: ConductorDao
){
    val conductores : Flow<List<Conductor>> = conductorDao.obtenerConductores()

    suspend fun almacenarConductores (conductores : List<Conductor>) {
        conductorDao.almacenarConductores(conductores)
    }

    suspend fun contarConductores () : Int {
        return conductorDao.contarConductores()
    }
}
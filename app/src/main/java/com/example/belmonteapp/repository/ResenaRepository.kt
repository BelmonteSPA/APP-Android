package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.ResenaDao
import com.example.belmonteapp.modelo.Resena
import kotlinx.coroutines.flow.Flow

class ResenaRepository(
    private val resenaDao: ResenaDao
) {
    val resenas: Flow<List<Resena>> = resenaDao.obtenerResenas()

    suspend fun almacenarResena(resena: Resena) {
        resenaDao.almacenarResena(resena)
    }
}
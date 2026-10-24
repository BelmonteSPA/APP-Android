package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NuevoViajeViewModel: ViewModel() {

    var destino by mutableStateOf("")
    var origen by mutableStateOf("")
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")

    fun agregarDestino(nuevoDestino: String){
        destino = nuevoDestino
    }
    fun agregarOrigen(nuevoOrigen: String){
        origen = nuevoOrigen
    }
    fun agregarFecha(nuevaFecha: String){
        fecha = nuevaFecha
    }
    fun agregarHora(nuevaHora: String){
        hora = nuevaHora
    }
}
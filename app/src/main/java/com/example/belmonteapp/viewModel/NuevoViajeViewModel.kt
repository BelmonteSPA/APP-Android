package com.example.belmonteapp.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint


class NuevoViajeViewModel: ViewModel() {

    var _puntoOrigen by mutableStateOf<GeoPoint?>(null)
        private set
    var _puntoDestino by mutableStateOf<GeoPoint?>(null)
        private set
    var origen by mutableStateOf("")
    var destino by mutableStateOf("")
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    var errorOrigen by mutableStateOf<String?>(null)
    var errorDestino by mutableStateOf<String?>(null)

    fun agregarDestino(nuevoDestino: String){
        destino = nuevoDestino
    }
    fun agregarOrigen(nuevoOrigen: String){
        origen = nuevoOrigen
    }
    fun agregarPotDestino(nuevoDestino: GeoPoint){
        _puntoDestino = nuevoDestino
    }
    fun agregarPotOrigen(nuevoOrigen: GeoPoint){
        _puntoOrigen = nuevoOrigen
    }

    fun agregarFecha(nuevaFecha: String){
        fecha = nuevaFecha
    }
    fun agregarHora(nuevaHora: String){
        hora = nuevaHora
    }
    /*fun crearNuevoViaje(mail: String, pass: String): Boolean{
        val viaje = //crear viaje
        return viaje != null
    }
    fun cancelar() {return viaje != null()}*/
    fun buscarCoordenadasDesdeTexto(context: Context, direccionTexto: String) {
        if (direccionTexto.isBlank()) return
    }
}

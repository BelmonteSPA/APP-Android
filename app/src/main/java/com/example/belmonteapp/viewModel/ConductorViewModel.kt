package com.example.belmonteapp.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ConductorViewModel {
    var destino by mutableStateOf("")
    var origen by mutableStateOf("")
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    // Estado que guarda la URI de la foto seleccionada
    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()
    // Función para actualizar la foto desde la pantalla
    fun cambiarFotoPerfil(nuevaUri: Uri?) {
        _imagenUri.value = nuevaUri
        // NOTA: Aquí también podrías mandar la foto a tu servidor/base de datos si lo necesitas
    }

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
package com.example.belmonteapp.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//Guarda el texto del ultimo QR escaneado
class QrViewModel : ViewModel() {

    private val _contenidoQR = MutableStateFlow("")
    val contenidoQR: StateFlow<String> = _contenidoQR.asStateFlow()

    fun guardarQR(contenido: String) {
        _contenidoQR.value = contenido
    }

    //Los QR de los autos dicen "BELMONTE:KDRT-42".
    //Devuelve la patente, o null si el QR no es de Belmonte.
    fun patente(): String? {
        val texto = _contenidoQR.value
        return if (texto.startsWith("BELMONTE:")) {
            texto.removePrefix("BELMONTE:").trim()
        } else {
            null
        }
    }
}
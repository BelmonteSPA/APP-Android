package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint
import com.example.belmonteapp.modelo.EstadoSeleccion
import android.content.Context
import android.location.Geocoder
import java.util.Locale


class NuevoViajeViewModel: ViewModel() {

    var _puntoOrigen by mutableStateOf<GeoPoint?>(null)
        private set
    var _puntoDestino by mutableStateOf<GeoPoint?>(null)
        private set
    var direccionOrigen by mutableStateOf("")
        private set

    var direccionDestino by mutableStateOf("")
        private set
    var estadoSeleccion by mutableStateOf(EstadoSeleccion.SELECCIONAR_ORIGEN)
        private set
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    var errorOrigen by mutableStateOf<String?>(null)
    var errorDestino by mutableStateOf<String?>(null)


    fun establecerOrigen(context: Context, punto: GeoPoint) {
        _puntoOrigen = punto
        direccionOrigen = obtenerDireccion(context, punto)
        estadoSeleccion = EstadoSeleccion.SELECCIONANDO_DESTINO
    }
    fun establecerDestino(context: Context, punto: GeoPoint) {
        _puntoDestino = punto
        direccionDestino = obtenerDireccion(context, punto)
        estadoSeleccion = EstadoSeleccion.LISTO
    }

    fun agregarFecha(nuevaFecha: String){
        fecha = nuevaFecha
    }
    fun agregarHora(nuevaHora: String){
        hora = nuevaHora
    }
    private fun obtenerDireccion(
        context: Context,
        punto: GeoPoint
    ): String {

        return try {

            val geocoder = Geocoder(
                context,
                Locale.getDefault()
            )

            val resultados = geocoder.getFromLocation(
                punto.latitude,
                punto.longitude,
                1
            )

            if (!resultados.isNullOrEmpty()) {

                resultados[0].getAddressLine(0)
                    ?: "Dirección desconocida"

            } else {

                "Dirección desconocida"
            }

        } catch (e: Exception) {

            "Dirección desconocida"
        }
    }


}

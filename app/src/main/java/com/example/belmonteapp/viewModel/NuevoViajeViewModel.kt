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
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import java.time.LocalDate
import java.time.LocalTime


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
    var marcadorOrigen by mutableStateOf<GeoPoint?>(null)
        private set

    var marcadorDestino by mutableStateOf<GeoPoint?>(null)
        private set
    var fecha by mutableStateOf("")
    var hora by mutableStateOf("")
    var errorOrigen by mutableStateOf<String?>(null)
    var errorDestino by mutableStateOf<String?>(null)
    var errorDistancia by mutableStateOf<String?>(null)
        private set

    fun establecerOrigen(context: Context, punto: GeoPoint) {
        _puntoOrigen = punto
        marcadorOrigen = punto
        direccionOrigen = obtenerDireccion(context, punto)
        estadoSeleccion = EstadoSeleccion.SELECCIONANDO_DESTINO
    }
    fun establecerDestino(context: Context, punto: GeoPoint) {
        _puntoDestino = punto
        marcadorDestino = punto
        direccionDestino = obtenerDireccion(context, punto)
        estadoSeleccion = EstadoSeleccion.LISTO
    }
    fun viajeValido(): Boolean {
        if (_puntoOrigen == null || _puntoDestino == null) {
            return false
        }

        val distancia = _puntoOrigen!!.distanceToAsDouble(
            _puntoDestino
        )

        return if (distancia < 50) {

            errorDistancia =
                "El destino debe estar a más de 50 metros del origen"

            false

        } else {

            errorDistancia = null

            true
        }
    }
    fun construirViaje(): Viajes? {
        val cuenta = SesionManager.cuentaActual ?: return null
        val origen = _puntoOrigen ?: return null
        val destino = _puntoDestino ?: return null

        return Viajes(
            codigo = "",
            fecha = LocalDate.now(),
            hora = LocalTime.now().withSecond(0).withNano(0),
            origen = direccionOrigen,
            destino = direccionDestino,
            latitudOrigen = origen.latitude,
            longitudOrigen = origen.longitude,
            latitudDestino = destino.latitude,
            longitudDestino = destino.longitude,
            pasajeroNombre = "${cuenta.nombres} ${cuenta.apellidos}",
            pasajeroTelefono = cuenta.telefono,
            pasajeroRut = cuenta.rut,
            estado = EstadoViaje.PENDIENTE
        )
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
    fun validarDistanciaMinima(): Boolean {

        if (_puntoOrigen == null || _puntoDestino == null) {
            return false
        }

        val distancia = _puntoOrigen!!.distanceToAsDouble(
            _puntoDestino
        )

        return distancia >= 50
    }
    fun reiniciarTodo() {
        _puntoOrigen = null
        _puntoDestino = null
        marcadorOrigen = null
        marcadorDestino = null
        direccionOrigen = ""
        direccionDestino = ""
        errorDistancia = null
        estadoSeleccion = EstadoSeleccion.SELECCIONAR_ORIGEN
    }

}

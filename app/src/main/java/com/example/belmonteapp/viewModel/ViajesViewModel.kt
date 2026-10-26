package com.example.belmonteapp.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belmonteapp.data.DataViajes
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.database.AppDatabase
import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.repository.ConductorRepository
import com.example.belmonteapp.repository.ViajeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import android.location.Location


//lo usan las pantallas del conductor y del admin
class ViajesViewModel (application: Application) : AndroidViewModel(application){

    private val viajeRepository : ViajeRepository
    private val conductorRepository : ConductorRepository

    val MisViajes  by mutableStateOf("")

    val viajes : StateFlow<List<Viajes>>
    val conductores : StateFlow<List<Conductor>>

    //id del conductor con la sesion iniciada
    //si no hay sesion se usa el 1 para poder probar
    val conductorId : Int
        get() = SesionManager.cuentaActual?.id ?: 1

    init {
        val database = AppDatabase.getDatabase(application)

        viajeRepository = ViajeRepository(database.viajeDao())
        conductorRepository = ConductorRepository(database.conductorDao())

        viajes = viajeRepository.viajes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        conductores = conductorRepository.conductores.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        cargarDatosDePrueba()
    }

    //la primera vez que se abre la app, esta vacia y se llena con datos fake
    private fun cargarDatosDePrueba(){
        viewModelScope.launch {
            if (conductorRepository.contarConductores() == 0){
                conductorRepository.almacenarConductores(DataViajes.conductores())
            }
            if (viajeRepository.contarViajes() == 0){
                viajeRepository.almacenarViajes(DataViajes.viajes())
            }
        }
    }

    //admin: asigna o cambia el conductor y el viaje queda ASIGNADO
    fun asignarConductor (viaje : Viajes, conductor : Conductor){
        val actualizado = viaje.copy(
            conductorId = conductor.id,
            conductorNombre = conductor.nombre,
            estado = EstadoViaje.ASIGNADO
        )

        viewModelScope.launch {
            viajeRepository.actualizarViaje(actualizado)
        }
    }

    //conductor ASIGNADO -> EN_CURSO
    fun iniciarViaje (viaje : Viajes){
        val actualizado = viaje.copy(
            estado = EstadoViaje.EN_CURSO,
            inicioMillis = System.currentTimeMillis()
        )

        viewModelScope.launch {
            viajeRepository.actualizarViaje(actualizado)
        }
    }

    //conductor EN_CURSO -> FINALIZADO
    fun finalizarViaje (viaje : Viajes){
        val actualizado = viaje.copy(
            estado = EstadoViaje.FINALIZADO,
            terminoMillis = System.currentTimeMillis()
        )

        viewModelScope.launch {
            viajeRepository.actualizarViaje(actualizado)
        }
    }
    //pasajero: guarda un viaje nuevo (PENDIENTE) y le asigna el siguiente codigo T-xxxx
    fun crearViaje(viaje: Viajes, onGuardado: () -> Unit = {}) {
        viewModelScope.launch {
            //lee los viajes que hay ahora en Room y busca el numero de codigo mas alto
            val existentes = viajeRepository.viajes.first()
            val ultimo = (existentes.maxOfOrNull {
                it.codigo.removePrefix("T-").toIntOrNull() ?: 0
            } ?: 0).coerceAtLeast(1039)

            viajeRepository.almacenarViaje(viaje.copy(codigo = "T-${ultimo + 1}"))
            onGuardado()
        }
    }
    //pasajero: viaje del cual mostrar el conductor (EN_CURSO, o si no, el ultimo)
    fun viajeConConductor(viajes: List<Viajes>, rutPasajero: String): Viajes? {
        if (rutPasajero.isBlank()) return null

        val mios = viajes.filter { it.pasajeroRut == rutPasajero && it.conductorId != null }
        val porFecha = compareBy<Viajes>({ it.fecha }, { it.hora })

        return mios.firstOrNull { it.estado == EstadoViaje.EN_CURSO }
            ?: mios.filter { it.estado == EstadoViaje.FINALIZADO }.maxWithOrNull(porFecha)
            ?: mios.maxWithOrNull(porFecha)
    }
    //pasajero: su viaje FINALIZADO mas reciente
    fun ultimoViajeFinalizado(viajes: List<Viajes>, rutPasajero: String): Viajes? {
        if (rutPasajero.isBlank()) return null

        return viajes
            .filter { it.pasajeroRut == rutPasajero && it.estado == EstadoViaje.FINALIZADO }
            .maxWithOrNull(compareBy<Viajes>({ it.fecha }, { it.hora }))
    }

    //distancia en linea recta entre origen y destino, en km
    fun distanciaKm(viaje: Viajes): Double {
        val resultado = FloatArray(1)
        Location.distanceBetween(
            viaje.latitudOrigen, viaje.longitudOrigen,
            viaje.latitudDestino, viaje.longitudDestino,
            resultado
        )
        return resultado[0] / 1000.0
    }
}
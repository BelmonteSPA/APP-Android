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
    /*
    fun crearViaje (){
        val nuevoViaje = Viajes(
        )
    }*/
}
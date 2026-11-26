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
import com.example.belmonteapp.modelo.Resena
import com.example.belmonteapp.repository.ResenaRepository
import com.example.belmonteapp.repository.SesionRepository


//lo usan las pantallas del conductor y del admin
class ViajesViewModel (application: Application) : AndroidViewModel(application){

    private val viajeRepository : ViajeRepository
    private val sesionRepository : SesionRepository

    val resenas : StateFlow<List<Resena>>
    val MisViajes  by mutableStateOf("")

    val viajes : StateFlow<List<Viajes>>
    val conductores : StateFlow<List<Conductor>>

    //id del conductor con la sesion iniciada
    //si no hay sesion se usa el 1 para poder probar
    val conductorId : Int
        get() = SesionManager.cuentaActual?.id ?: 1
    val conductorRepository : ConductorRepository
    private val _resenaRepository : ResenaRepository


    init {
        val database = AppDatabase.getDatabase(application)

        viajeRepository = ViajeRepository(database.viajeDao(), database.sesionDao())
        conductorRepository = ConductorRepository(database.conductorDao(), database.sesionDao())
        sesionRepository = SesionRepository(database.sesionDao())

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

        cargarDatos()
        _resenaRepository = ResenaRepository(database.resenaDao())

        resenas = _resenaRepository.resenas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    //Con servidor: descarga los viajes del backend.
    //Sin servidor: la primera vez llena Room con datos fake.
    private fun cargarDatos(){
        viewModelScope.launch {
            if (conductorRepository.contarConductores() == 0){
                conductorRepository.almacenarConductores(DataViajes.conductores())
            }
            if (viajeRepository.hayServidor()) {
                sincronizar()
            } else if (viajeRepository.contarViajes() == 0){
                viajeRepository.almacenarViajes(DataViajes.viajes())
            }
        }
    }

    //Vuelve a pedir los viajes al backend (si falla se queda con lo que hay en Room)
    fun sincronizar() {
        viewModelScope.launch {
            try {
                viajeRepository.sincronizar(SesionManager.cuentaActual?.rut ?: "")
                conductorRepository.sincronizar()
            } catch (e: Exception) {
                //sin conexion: se siguen mostrando los datos guardados
            }
        }
    }

    //Cerrar sesion (los 3 perfiles): borra el token de Room y la cuenta activa.
    //onListo: la pantalla navega al login recien cuando se borro el token
    fun cerrarSesion(onListo: () -> Unit) {
        viewModelScope.launch {
            sesionRepository.cerrarSesion()
            SesionManager.cerrarSesion()
            onListo()
        }
    }
    //admin: asigna o cambia el conductor y el viaje queda ASIGNADO
    //onListo: la pantalla navega recien cuando el cambio quedo guardado
    //(si navega antes, el ViewModel se destruye y se cancela la llamada al servidor)
    fun asignarConductor (viaje : Viajes, conductor : Conductor, onListo: () -> Unit = {}){
        val actualizado = viaje.copy(
            conductorId = conductor.id,
            conductorNombre = conductor.nombre,
            estado = EstadoViaje.ASIGNADO
        )

        viewModelScope.launch {
            if (viajeRepository.hayServidor()) {
                try {
                    viajeRepository.asignarEnServidor(viaje, conductor.id)
                } catch (e: Exception) {
                    //el servidor rechazo el cambio o no respondio: no se modifica nada
                    return@launch
                }
            } else {
                viajeRepository.actualizarViaje(actualizado)
            }
            onListo()
        }
    }

    //Conductor: ASIGNADO -> EN_CURSO, solo si no tiene otro viaje EN_CURSO.
    //onIniciado se ejecuta cuando el cambio ya quedo guardado en la base de datos.
    fun iniciarViaje(viaje: Viajes, onIniciado: () -> Unit = {}) {
        viewModelScope.launch {
            //Se revisa en la base de datos (no en la pantalla) si ya tiene un viaje en curso
            val tieneOtroEnCurso = viajeRepository.viajes.first().any {
                it.conductorId == viaje.conductorId &&
                        it.estado == EstadoViaje.EN_CURSO &&
                        it.id != viaje.id
            }
            if (tieneOtroEnCurso) return@launch

            if (viajeRepository.hayServidor()) {
                try {
                    viajeRepository.iniciarEnServidor(viaje)
                } catch (e: Exception) {
                    return@launch
                }
            } else {
                val actualizado = viaje.copy(
                    estado = EstadoViaje.EN_CURSO,
                    inicioMillis = System.currentTimeMillis()
                )
                viajeRepository.actualizarViaje(actualizado)
            }

            //Recien aqui se avisa a la pantalla que puede navegar
            onIniciado()
        }
    }

    //conductor EN_CURSO -> FINALIZADO (onListo igual que en asignarConductor)
    fun finalizarViaje (viaje : Viajes, onListo: () -> Unit = {}){
        val actualizado = viaje.copy(
            estado = EstadoViaje.FINALIZADO,
            terminoMillis = System.currentTimeMillis()
        )

        viewModelScope.launch {
            if (viajeRepository.hayServidor()) {
                try {
                    viajeRepository.finalizarEnServidor(viaje)
                } catch (e: Exception) {
                    //no se modifica nada
                    return@launch
                }
            } else {
                viajeRepository.actualizarViaje(actualizado)
            }
            onListo()
        }
    }

    //pasajero: guarda un viaje nuevo (PENDIENTE) y le asigna el siguiente codigo T-xxxx
    fun crearViaje(viaje: Viajes, onGuardado: () -> Unit = {}) {
        viewModelScope.launch {
            if (viajeRepository.hayServidor()) {
                //el backend le asigna el id; el codigo se arma con ese id
                try {
                    viajeRepository.crearEnServidor(viaje)
                } catch (e: Exception) {
                    return@launch
                }
                onGuardado()
                return@launch
            }

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

    //pasajero: evalua un viaje FINALIZADO (una sola vez por viaje)
    fun enviarResena(viaje: Viajes, estrellas: Int, comentario: String, onListo: () -> Unit = {}) {
        val conductorId = viaje.conductorId ?: return
        if (viaje.estado != EstadoViaje.FINALIZADO || estrellas !in 1..5) return

        viewModelScope.launch {
            val yaExiste = _resenaRepository.resenas.first().any { it.viajeId == viaje.id }
            if (yaExiste) return@launch

            val guardada = try {
                _resenaRepository.almacenarResena(
                    Resena(
                        viajeId = viaje.id,
                        conductorId = conductorId,
                        pasajeroRut = viaje.pasajeroRut,
                        estrellas = estrellas,
                        comentario = comentario.trim()
                    )
                )
                true
            } catch (e: Exception) {
                false
            }

            if (guardada) onListo()
        }
    }

}
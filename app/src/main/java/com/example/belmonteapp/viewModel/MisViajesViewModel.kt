package com.example.belmonteapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belmonteapp.data.DataViajes
import com.example.belmonteapp.database.AppDatabase
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.repository.ViajeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.belmonteapp.data.SesionManager

class MisViajesViewModel(application: Application) : AndroidViewModel(application) {

    private val viajeRepository = ViajeRepository(AppDatabase.getDatabase(application).viajeDao())

    // Estado para el texto del buscador
    val textoBusqueda = MutableStateFlow("")

    // RUT del pasajero que inició sesión: solo se muestran sus viajes
    private val rutPasajero: String
        get() = SesionManager.cuentaActual?.rut ?: ""

    init {
        viewModelScope.launch {
            if (viajeRepository.contarViajes() == 0) {
                viajeRepository.almacenarViajes(DataViajes.viajes())
            }
        }
    }

    // Lista filtrada en tiempo real
    // Lista filtrada en tiempo real: solo los viajes de este pasajero y que coincidan con la búsqueda
    val viajesFiltrados: StateFlow<List<Viajes>> = combine(
        textoBusqueda,
        viajeRepository.viajes
    ) { query, lista ->
        lista.filter { viaje ->
            viaje.pasajeroRut == rutPasajero && viaje.coincideConBusqueda(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTextoBusquedaChange(nuevoTexto: String) {
        textoBusqueda.value = nuevoTexto
    }

}

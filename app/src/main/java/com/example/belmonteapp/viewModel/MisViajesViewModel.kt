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

class MisViajesViewModel(application: Application) : AndroidViewModel(application) {

    private val viajeRepository = ViajeRepository(AppDatabase.getDatabase(application).viajeDao())

    // Estado para el texto del buscador
    val textoBusqueda = MutableStateFlow("")

    init {
        viewModelScope.launch {
            if (viajeRepository.contarViajes() == 0) {
                viajeRepository.almacenarViajes(DataViajes.viajes())
            }
        }
    }

    // Lista filtrada en tiempo real
    val viajesFiltrados: StateFlow<List<Viajes>> = combine(
        textoBusqueda,
        viajeRepository.viajes
    ) { query, lista ->
        val datosAMostrar = if (lista.isEmpty()) DataViajes.viajes() else lista
        datosAMostrar.filter { viaje ->
            viaje.coincideConBusqueda(query)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DataViajes.viajes()
    )

    fun onTextoBusquedaChange(nuevoTexto: String) {
        textoBusqueda.value = nuevoTexto
    }

}

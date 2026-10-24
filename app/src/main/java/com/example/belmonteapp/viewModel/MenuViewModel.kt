package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.modelo.Tipo

class MenuViewModel: ViewModel(

) {
    private val registroViewModel = RegistroViewModel()
    val cuentas by mutableStateOf("")


    // Propiedad en el ViewModel que obtiene la ruta según el tipo de cuenta logueada
    val rutaPerfil: Tipo
        get() = SesionManager.cuentaActual?.tipo ?: Tipo.PASAJERO




}
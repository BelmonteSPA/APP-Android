package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.modelo.Usuario

class LoginViewModel: ViewModel() {
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)

    fun cambiarMail(nuevoMail: String){
        mail = nuevoMail
    }

    fun cambiarPass(nuevaPass: String){
        pass = nuevaPass
    }


}
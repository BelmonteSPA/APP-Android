package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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

    var usuarioCorrecto = Usuario(0, "admin", "123")

    fun login(): Boolean{
        var validador = false
        if(mail == usuarioCorrecto.mail && pass == usuarioCorrecto.pass){
            validador = true
        }
        return validador
    }
}
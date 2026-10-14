package com.example.belmonteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
class RegistroViewModel {
    var nombres by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)

    fun agregarNombre(nuevoNombre: String){
        nombres = nuevoNombre
    }
    fun agregarApellido(nuevoApellido: String){
        apellidos = nuevoApellido
    }
    fun agregarMail(nuevoMail: String){
        mail = nuevoMail
    }
    fun agregarPass(nuevaPass: String){
        pass = nuevaPass
    }


}
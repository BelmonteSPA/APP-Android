package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.belmonteapp.modelo.Cuenta
import com.example.belmonteapp.modelo.Usuario
import androidx.lifecycle.ViewModel

class RegistroViewModel: ViewModel() {
    var nombres by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var rut by mutableStateOf("")
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)
    var codigo by mutableStateOf("")

    fun agregarNombre(nuevoNombre: String){
        nombres = nuevoNombre
    }
    fun agregarApellido(nuevoApellido: String){
        apellidos = nuevoApellido
    }
    fun agregarRut(nuevoRut: String){
        rut = nuevoRut
    }
    fun agregarMail(nuevoMail: String){
        mail = nuevoMail
    }
    fun agregarPass(nuevaPass: String) {
        pass = nuevaPass
    }
    fun agregarCodigo(nuevoCodigo: String) {
        codigo = nuevoCodigo
    }

    fun crearCuenta() {

        val nuevoUsuario = Usuario(
            mail = mail,
            pass = pass
        )

        val nuevaCuenta = Cuenta(
            nombres = nombres,
            apellidos = apellidos,
            rut = rut,
            usuario = nuevoUsuario
        )


    }
}
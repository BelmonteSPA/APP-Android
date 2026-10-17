package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.belmonteapp.modelo.Cuenta
import com.example.belmonteapp.modelo.Usuario
import androidx.lifecycle.ViewModel
import com.example.belmonteapp.data.DataP
import com.example.belmonteapp.data.LocationDataRepository
import com.example.belmonteapp.modelo.Tipo

class RegistroViewModel: ViewModel() {
    var nombres by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var rut by mutableStateOf("")
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)


    var direccion by mutableStateOf("")
    var codigo by mutableStateOf("")
    var region by mutableStateOf("")
    var comuna by mutableStateOf("")
    var comunas by mutableStateOf(listOf<String>())
    var perfilCorrecto by mutableStateOf(false)
    var cuentaActual by mutableStateOf<Cuenta?>(null)
    val regiones = LocationDataRepository.regiones.map { it.nombre }

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
    fun cambiarRegion(nuevaRegion: String) {
        region = nuevaRegion
        comuna = "" // limpia la comuna
        comunas = LocationDataRepository.getComunas(nuevaRegion)
    }

    fun cambiarComuna(nuevaComuna: String) {
        comuna = nuevaComuna
    }
    fun cambiarNombres(nuevo: String) {
        nombres = nuevo
    }
    fun cambiarApellidos(nuevo: String) {
        apellidos = nuevo
    }
    fun cambiarRut(nuevo: String) {
        rut = nuevo
    }
    fun cambiarCorreo(nuevo: String) {
        mail = nuevo
    }
    fun cambiarPass(nuevo: String) {
        pass = nuevo
    }
    fun cambiarDireccion(nuevo: String) {
        direccion = nuevo
    }
    fun validar(): Boolean {
        return region.isNotEmpty() && comuna.isNotEmpty()
    }
    fun perfil(): Boolean {
        val cuenta = DataP.UsuariosPrueba.cuentas.find {
            it.usuario.mail == mail && it.usuario.pass == pass
        }
        cuentaActual = cuenta
        return cuenta != null
    }

}
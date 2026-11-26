package com.example.belmonteapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.belmonteapp.modelo.Cuenta
import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.modelo.Tipo
import com.example.belmonteapp.modelo.Usuario

object SesionManager {
    val cuentas = mutableStateListOf<Cuenta>().apply {
        addAll(DataP.UsuariosPrueba.cuentas)
    }

    var cuentaActual by mutableStateOf<Cuenta?>(null)
        private set

    //Modo local (sin servidor): busca la cuenta en los datos de prueba
    fun iniciarSesion(mail: String, pass: String): Boolean {
        val cuenta = cuentas.find { it.usuario.mail == mail && it.usuario.pass == pass }
        cuentaActual = cuenta
        return cuenta != null
    }

    //Modo servidor: el login ya lo valido ms-auth.
    //Los datos del perfil (RUT, region, etc.) salen de la cuenta local con el mismo correo;
    //si no existe, se arma una cuenta basica con lo que entrego el backend.
    fun iniciarSesionServidor(sesion: Sesion) {
        val tipo = when (sesion.rol) {
            "CONDUCTOR" -> Tipo.CONDUCTOR
            "ADMIN" -> Tipo.ADMINISTRADOR
            else -> Tipo.PASAJERO
        }
        val local = cuentas.find { it.usuario.mail.equals(sesion.email, ignoreCase = true) }

        cuentaActual = local ?: Cuenta(
            id = sesion.idReferencia,
            nombres = sesion.email.substringBefore("@"),
            apellidos = "",
            rut = "Sin RUT",
            usuario = Usuario(0, sesion.email, ""),
            tipo = tipo
        )
    }

    fun registrar(cuenta: Cuenta) {
        val nueva = cuenta.copy(id = (cuentas.maxOfOrNull { it.id } ?: -1) + 1)
        cuentas.add(nueva)
        cuentaActual = nueva
    }

    fun actualizarCuenta(nueva: Cuenta) {
        val i = cuentas.indexOfFirst { it.id == nueva.id }
        if (i >= 0) cuentas[i] = nueva
        cuentaActual = nueva
    }

    fun cerrarSesion() { cuentaActual = null }
}
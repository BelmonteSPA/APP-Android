package com.example.belmonteapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.belmonteapp.modelo.Cuenta

object SesionManager {
    val cuentas = mutableStateListOf<Cuenta>().apply {
        addAll(DataP.UsuariosPrueba.cuentas)
    }

    var cuentaActual by mutableStateOf<Cuenta?>(null)
        private set

    fun iniciarSesion(mail: String, pass: String): Boolean {
        val cuenta = cuentas.find { it.usuario.mail == mail && it.usuario.pass == pass }
        cuentaActual = cuenta
        return cuenta != null
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
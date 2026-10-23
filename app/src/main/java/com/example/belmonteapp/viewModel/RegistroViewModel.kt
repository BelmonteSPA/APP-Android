package com.example.belmonteapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.belmonteapp.modelo.Cuenta
import com.example.belmonteapp.modelo.Usuario
import androidx.lifecycle.ViewModel
import com.example.belmonteapp.data.DataP
import com.example.belmonteapp.data.LocationDataRepository
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.modelo.Tipo

class RegistroViewModel: ViewModel() {
    var nombres by mutableStateOf("")
    var apellidos by mutableStateOf("")
    var rut by mutableStateOf("")
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var confirmarPass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)
    var telefono by mutableStateOf("")


    var direccion by mutableStateOf("")
    var codigo by mutableStateOf("")
    var region by mutableStateOf("")
    var comuna by mutableStateOf("")
    var comunas by mutableStateOf(listOf<String>())
    var perfilCorrecto by mutableStateOf(false)
    var cuentaActual by mutableStateOf<Cuenta?>(null)
    val regiones = LocationDataRepository.regiones.map { it.nombre }

    var errorPerfil by mutableStateOf("")
    var errorCodigo by mutableStateOf(false)
    var errorRegistro by mutableStateOf("")


    fun cargarDesdeSesion() {
        val c = SesionManager.cuentaActual ?: return
        nombres = c.nombres
        apellidos = c.apellidos
        rut = c.rut
        mail = c.usuario.mail
        pass = c.usuario.pass
        direccion = c.direccion
        region = c.region
        comunas = LocationDataRepository.getComunas(c.region)
        comuna = c.comuna
        telefono = c.telefono
        errorPerfil = ""
    }

    fun cancelarEdicion() = cargarDesdeSesion()

    fun guardarCambios(): Boolean {
        val actual = SesionManager.cuentaActual ?: return false
        errorPerfil = when {
            nombres.isBlank() || apellidos.isBlank() || rut.isBlank() -> "Completa nombres, apellidos y RUT"
            !mail.contains("@") -> "Correo inválido"
            pass.isBlank() -> "La contraseña no puede estar vacía"
            telefono.isBlank() -> "El teléfono no puede estar vacío"
            direccion.isBlank() -> "La dirección no puede estar vacía"
            region.isEmpty() || comuna.isEmpty() -> "Selecciona región y comuna"
            else -> ""
        }
        if (errorPerfil.isNotEmpty()) return false

        SesionManager.actualizarCuenta(
            actual.copy(
                nombres = nombres.trim(),
                apellidos = apellidos.trim(),
                rut = rut.trim(),
                usuario = actual.usuario.copy(mail = mail.trim(), pass = pass),
                region = region,
                comuna = comuna,
                direccion = direccion.trim(),
                telefono = telefono.trim()
            )
        )
        return true
    }

    fun crearCuenta(): Boolean {
        errorRegistro = ""

        if (nombres.isBlank() || apellidos.isBlank() || rut.isBlank() ||
            mail.isBlank() || pass.isBlank()) {
            errorRegistro = "Completa todos los campos obligatorios"
            return false
        }
        if (SesionManager.cuentas.any { it.usuario.mail == mail }) {
            errorRegistro = "Ya existe una cuenta con ese correo"
            return false
        }

        // Código vacío = pasajero, válido = conductor, inválido = error
        val tipoNuevo = when {
            codigo.isBlank() -> Tipo.PASAJERO
            DataP.CodigosTrabajador.esValido(codigo) -> Tipo.CONDUCTOR
            else -> {
                errorCodigo = true
                return false
            }
        }
        errorCodigo = false

        SesionManager.registrar(
            Cuenta(
                nombres = nombres, apellidos = apellidos, rut = rut, telefono = telefono,
                usuario = Usuario(mail = mail, pass = pass),
                tipo = tipoNuevo,
                region = region, comuna = comuna, direccion = direccion
            )
        )
        return true
    }


    fun agregarNombre(nuevoNombre: String) {
        nombres = nuevoNombre
    }

    fun agregarApellido(nuevoApellido: String) {
        apellidos = nuevoApellido
    }

    fun agregarRut(nuevoRut: String) {
        rut = nuevoRut
    }

    fun agregarMail(nuevoMail: String) {
        mail = nuevoMail
    }

    fun agregarTelefono(nuevoTelefono: String) {
        telefono = nuevoTelefono
    }
    fun agregarDireccion(nuevaDireccion: String) {
        direccion = nuevaDireccion
    }

    fun agregarPass(nuevaPass: String) {
        pass = nuevaPass
    }

    fun agregarConfirmarPass(nuevaPass: String) {
        confirmarPass = nuevaPass
    }

    // La "tercera variable": se calcula sola, no se guarda
    val passCoinciden: Boolean
        get() = pass.isNotEmpty() && pass == confirmarPass

    fun agregarCodigo(nuevoCodigo: String) {
        codigo = nuevoCodigo
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
    fun cambiarTelefono(nuevo: String) {
        telefono = nuevo
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

    fun validarCodigoTrabajador(): Boolean {
        // Campo vacío = sigue siendo pasajero (el código es opcional)
        if (codigo.isBlank()) {//Comprueba si la variable está vacía o contiene solo espacios en blanco.
            errorCodigo = false
            return true
        }
        return if (DataP.CodigosTrabajador.esValido(codigo)) {
            errorCodigo = false
            cuentaActual = cuentaActual?.copy(tipo = Tipo.CONDUCTOR)
            true
        } else {
            errorCodigo = true
            false
        }
    }
    fun iniciarSesion(mailIngresado: String, passIngresada: String): Boolean {
        val cuenta = DataP.UsuariosPrueba.cuentas.find {
            it.usuario.mail == mailIngresado && it.usuario.pass == passIngresada
        }
        cuentaActual = cuenta
        return cuenta != null
    }


}
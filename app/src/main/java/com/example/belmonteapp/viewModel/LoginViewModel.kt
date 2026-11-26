package com.example.belmonteapp.viewModel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.database.AppDatabase
import com.example.belmonteapp.repository.ConductorRepository
import com.example.belmonteapp.repository.SesionRepository
import com.example.belmonteapp.repository.ViajeRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    var mail by mutableStateOf("")
    var pass by mutableStateOf("")
    var textoVisible by mutableStateOf(false)
    var cargando by mutableStateOf(false) //true mientras espera la respuesta del servidor

    private val database = AppDatabase.getDatabase(application)
    private val sesionRepository = SesionRepository(database.sesionDao())
    private val viajeRepository = ViajeRepository(database.viajeDao(), database.sesionDao())
    private val conductorRepository = ConductorRepository(database.conductorDao(), database.sesionDao())

    fun cambiarMail(nuevoMail: String){
        mail = nuevoMail
    }

    fun cambiarPass(nuevaPass: String){
        pass = nuevaPass
    }

    //1. Intenta el login en el backend (ms-auth).
    //2. Si el servidor no responde, usa las cuentas de prueba locales (modo sin conexion).
    //onExito se llama solo si el correo y la clave son correctos.
    fun iniciarSesion(onExito: () -> Unit) {
        textoVisible = false
        cargando = true

        viewModelScope.launch {
            val correcto = try {
                val sesion = sesionRepository.login(mail.trim(), pass)
                SesionManager.iniciarSesionServidor(sesion)
                descargarDatos()
                true
            } catch (e: HttpException) {
                false //401: correo o clave incorrectos
            } catch (e: Exception) {
                SesionManager.iniciarSesion(mail.trim(), pass) //servidor apagado: cuentas locales
            }

            cargando = false
            if (correcto) onExito() else textoVisible = true
        }
    }

    //Deja en Room los viajes (y conductores, si es admin) que vienen del backend
    private suspend fun descargarDatos() {
        try {
            viajeRepository.sincronizar(SesionManager.cuentaActual?.rut ?: "")
            conductorRepository.sincronizar()
        } catch (e: Exception) {
            //si falla la descarga se sigue con lo que ya habia en Room
        }
    }
}
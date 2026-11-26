package com.example.belmonteapp.data

import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.modelo.Tipo
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

//Prueba el inicio de sesion local (cuentas de prueba) y con los datos del backend
class SesionManagerTest : StringSpec({

    "iniciarSesion() local acepta la cuenta de prueba del conductor" {
        SesionManager.iniciarSesion("conductor@belmonte.cl", "123") shouldBe true
        SesionManager.cuentaActual?.tipo shouldBe Tipo.CONDUCTOR
    }

    "iniciarSesion() local rechaza una clave incorrecta" {
        SesionManager.iniciarSesion("conductor@belmonte.cl", "999") shouldBe false
        SesionManager.cuentaActual shouldBe null
    }

    "iniciarSesionServidor() usa los datos locales de la cuenta con el mismo correo" {
        SesionManager.iniciarSesionServidor(
            Sesion(token = "t", email = "pasajero@belmonte.cl", rol = "PASAJERO", idReferencia = 1)
        )

        SesionManager.cuentaActual?.tipo shouldBe Tipo.PASAJERO
        SesionManager.cuentaActual?.rut shouldBe "12.345.678-9"
    }

    "iniciarSesionServidor() arma una cuenta basica si el correo no existe en la app" {
        SesionManager.iniciarSesionServidor(
            Sesion(token = "t", email = "conductor5@ejemplo.test", rol = "CONDUCTOR", idReferencia = 5)
        )

        SesionManager.cuentaActual?.id shouldBe 5
        SesionManager.cuentaActual?.tipo shouldBe Tipo.CONDUCTOR
    }

    "cerrarSesion() deja la app sin cuenta activa" {
        SesionManager.iniciarSesion("admin@belmonte.cl", "123")

        SesionManager.cerrarSesion()

        SesionManager.cuentaActual shouldBe null
    }
})

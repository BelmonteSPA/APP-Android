package com.example.belmonteapp.modelo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

//Prueba el buscador de viajes (coincideConBusqueda)
class ViajesTest : StringSpec({

    val viaje = Viajes(
        codigo = "T-1048",
        fecha = LocalDate.of(2026, 11, 26),
        hora = LocalTime.of(11, 20),
        origen = "Aeropuerto SCL",
        destino = "Torre Costanera",
        latitudOrigen = -33.3965,
        longitudOrigen = -70.7932,
        latitudDestino = -33.4173,
        longitudDestino = -70.6064,
        numeroVuelo = "H2 247",
        pasajeroNombre = "Diego Fuentes",
        pasajeroRut = "12.345.678-9",
        estado = EstadoViaje.ASIGNADO
    )

    "una busqueda vacia muestra todos los viajes" {
        viaje.coincideConBusqueda("   ") shouldBe true
    }

    "encuentra el viaje por codigo, destino o vuelo sin importar mayusculas" {
        viaje.coincideConBusqueda("t-1048") shouldBe true
        viaje.coincideConBusqueda("costanera") shouldBe true
        viaje.coincideConBusqueda("h2 247") shouldBe true
    }

    "no encuentra el viaje si el texto no coincide con ningun dato" {
        viaje.coincideConBusqueda("Valparaiso") shouldBe false
    }
})

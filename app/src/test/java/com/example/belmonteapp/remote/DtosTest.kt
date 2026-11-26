package com.example.belmonteapp.remote

import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalTime

//Prueba las conversiones entre el JSON del backend (DTO) y la entidad de Room
class DtosTest : StringSpec({

    //viaje como lo entrega ms-viajes
    val dto = ViajeDto(
        id = 5,
        idPasajero = 1,
        idConductor = 1,
        fecha = "2026-11-26",
        hora = "09:15:00",
        origen = "Hotel Belmonte",
        destino = "Oficinas Las Condes",
        numeroVuelo = null,
        latitudOrigen = -33.4089,
        longitudOrigen = -70.5676,
        latitudDestino = -33.4012,
        longitudDestino = -70.5738,
        estado = "EN_CURSO",
        inicio = null,
        termino = null,
        nombrePasajero = "David Patricio Hernandez Valenzuela",
        telefonoPasajero = "+56 9 1234 5678",
        nombreConductor = "Juan Pérez"
    )

    "aViaje() convierte el JSON del backend en un Viajes de Room" {
        val viaje = dto.aViaje("12.345.678-9")

        viaje.id shouldBe 5
        viaje.codigo shouldBe "T-1044"
        viaje.fecha shouldBe LocalDate.of(2026, 11, 26)
        viaje.hora shouldBe LocalTime.of(9, 15)
        viaje.estado shouldBe EstadoViaje.EN_CURSO
        viaje.pasajeroNombre shouldBe "David Patricio Hernandez Valenzuela"
        viaje.pasajeroRut shouldBe "12.345.678-9"
        viaje.conductorId shouldBe 1
    }

    "aViaje() deja textos vacios y 0 cuando el backend manda null" {
        val pendiente = dto.copy(idConductor = null, nombreConductor = null, estado = "PENDIENTE")

        val viaje = pendiente.aViaje()

        viaje.numeroVuelo shouldBe ""
        viaje.conductorId shouldBe null
        viaje.conductorNombre shouldBe ""
        viaje.inicioMillis shouldBe 0L
        viaje.terminoMillis shouldBe 0L
    }

    "aNuevoViajeRequest() arma el cuerpo del POST con el id del pasajero" {
        val viaje = Viajes(
            codigo = "",
            fecha = LocalDate.of(2026, 11, 27),
            hora = LocalTime.of(14, 30),
            origen = "Torre Costanera",
            destino = "Aeropuerto SCL",
            latitudOrigen = -33.4173,
            longitudOrigen = -70.6064,
            latitudDestino = -33.3930,
            longitudDestino = -70.7858,
            pasajeroNombre = "David",
            pasajeroRut = "12.345.678-9"
        )

        val request = viaje.aNuevoViajeRequest(idPasajero = 1)

        request.idPasajero shouldBe 1
        request.fecha shouldBe "2026-11-27"
        request.hora shouldBe "14:30"
        request.destino shouldBe "Aeropuerto SCL"
    }
})

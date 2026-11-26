package com.example.belmonteapp.repository

import com.example.belmonteapp.dao.SesionDao
import com.example.belmonteapp.dao.ViajeDao
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Sesion
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.remote.ApiService
import com.example.belmonteapp.remote.ViajeDto
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import java.time.LocalDate
import java.time.LocalTime

//Prueba el repositorio de viajes simulando la API (MockK) y los DAO de Room
class ViajeRepositoryTest : StringSpec({

    //viaje que "responde" el backend
    fun viajeDto(id: Int, estado: String) = ViajeDto(
        id = id, idPasajero = 1, idConductor = 1,
        fecha = "2026-11-26", hora = "11:20:00",
        origen = "Aeropuerto SCL", destino = "Torre Costanera", numeroVuelo = "H2 247",
        latitudOrigen = -33.3965, longitudOrigen = -70.7932,
        latitudDestino = -33.4173, longitudDestino = -70.6064,
        estado = estado, inicio = null, termino = null,
        nombrePasajero = "Diego Fuentes", telefonoPasajero = "+56 9 7766 5544",
        nombreConductor = "Juan Pérez"
    )

    val sesionConductor = Sesion(token = "abc123", email = "conductor@belmonte.cl", rol = "CONDUCTOR", idReferencia = 1)

    "sincronizar() pide los viajes del conductor con su token y los guarda en Room" {
        // 1. Simulamos la API, los DAO y la sesion guardada
        val mockApi = mockk<ApiService>()
        val viajeDao = mockk<ViajeDao>(relaxed = true)
        val sesionDao = mockk<SesionDao>()
        coEvery { sesionDao.obtener() } returns sesionConductor
        coEvery { mockApi.viajesDelConductor("Bearer abc123", 1) } returns listOf(viajeDto(7, "ASIGNADO"))

        // 2. Creamos el repositorio inyectando el mock
        val repo = ViajeRepository(viajeDao, sesionDao, mockApi)

        // 3. Ejecutamos y verificamos
        runTest {
            repo.sincronizar("")
        }
        coVerify { mockApi.viajesDelConductor("Bearer abc123", 1) }
        coVerify { viajeDao.almacenarViajes(match { it.size == 1 && it[0].id == 7 && it[0].codigo == "T-1046" }) }
        coVerify { viajeDao.borrarViajesExcepto(listOf(7)) }
    }

    "sincronizar() no llama al backend si no hay sesion (modo local)" {
        val mockApi = mockk<ApiService>(relaxed = true)
        val viajeDao = mockk<ViajeDao>(relaxed = true)
        val sesionDao = mockk<SesionDao>()
        coEvery { sesionDao.obtener() } returns null

        val repo = ViajeRepository(viajeDao, sesionDao, mockApi)

        runTest {
            repo.sincronizar("")
            repo.hayServidor() shouldBe false
        }
        coVerify(exactly = 0) { mockApi.viajesDelConductor(any(), any()) }
        coVerify(exactly = 0) { viajeDao.almacenarViajes(any()) }
    }

    "iniciarEnServidor() guarda en Room el viaje EN_CURSO que responde el backend" {
        val mockApi = mockk<ApiService>()
        val viajeDao = mockk<ViajeDao>(relaxed = true)
        val sesionDao = mockk<SesionDao>()
        coEvery { sesionDao.obtener() } returns sesionConductor
        coEvery { mockApi.iniciarViaje("Bearer abc123", 7) } returns viajeDto(7, "EN_CURSO")

        val repo = ViajeRepository(viajeDao, sesionDao, mockApi)
        val viajeAsignado = Viajes(
            id = 7, codigo = "T-1046",
            fecha = LocalDate.of(2026, 11, 26), hora = LocalTime.of(11, 20),
            origen = "Aeropuerto SCL", destino = "Torre Costanera",
            latitudOrigen = -33.3965, longitudOrigen = -70.7932,
            latitudDestino = -33.4173, longitudDestino = -70.6064,
            pasajeroNombre = "Diego Fuentes", pasajeroRut = "",
            conductorId = 1, estado = EstadoViaje.ASIGNADO
        )

        runTest {
            repo.iniciarEnServidor(viajeAsignado)
        }
        coVerify { viajeDao.almacenarViaje(match { it.id == 7 && it.estado == EstadoViaje.EN_CURSO }) }
    }
})

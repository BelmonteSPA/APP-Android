package com.example.belmonteapp.data

import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalTime


object DataViajes {

    private data class Lugar (
        val nombre : String,
        val latitud : Double,
        val longitud : Double
    )

    private val SCL_T1 = Lugar("Aeropuerto SCL, Terminal 1, Pudahuel", -33.3965, -70.7932)
    private val SCL_T2 = Lugar("Aeropuerto SCL, Terminal 2, Pudahuel", -33.3930, -70.7858)
    private val HOTEL = Lugar("Hotel Belmonte, Av. Apoquindo 4500, Las Condes", -33.4089, -70.5676)
    private val LAS_CONDES = Lugar("Oficinas Las Condes, Av. Kennedy 5600, Las Condes", -33.4012, -70.5738)
    private val TORRE = Lugar("Torre Costanera, Av. Andrés Bello 2425, Providencia", -33.4173, -70.6064)

    //El conductor con "id" 1 coincide con "conductor@belmonte.cl" de DataP
    private val juan = Conductor(1, "Juan Pérez", "Hyundai H-1", "KDRT-42", "11.111.111-1", "+56 9 5555 2001")
    private val pedro = Conductor(102, "Pedro Vidal", "Kia Carnival", "PLZK-07", "13.456.789-2", "+56 9 5555 2002")
    private val carla = Conductor(103, "Carla Muñoz", "Toyota Hiace", "LRWP-18", "16.789.012-3", "+56 9 5555 2003")
    private val rodrigo = Conductor(104, "Rodrigo Silva", "Hyundai Staria", "SDGH-33", "14.567.890-K", "+56 9 5555 2004")

    fun conductores(): List<Conductor> {
        return listOf(juan, pedro, carla, rodrigo)
    }

    private fun fecha(dias: Long): LocalDate {
        return LocalDate.now().plusDays(dias)
    }

    private fun viaje(
        codigo: String,
        dias: Long,
        hora: String,
        origen: Lugar,
        destino: Lugar,
        pasajero: String,
        telefono: String,
        estado: EstadoViaje,
        conductor: Conductor? = null,
        vuelo: String = "",
        inicioHaceMin: Long = 0,
        duracionMin: Long = 0,
        rut: String = ""
    ): Viajes {
        val ahora = System.currentTimeMillis()
        val inicio = if (inicioHaceMin > 0) ahora - inicioHaceMin * 60_000 else 0L
        val termino = if (duracionMin > 0) inicio + duracionMin * 60_000 else 0L

        return Viajes(
            codigo = codigo,
            fecha = fecha(dias),
            hora = LocalTime.parse(hora),
            origen = origen.nombre,
            destino = destino.nombre,
            latitudOrigen = origen.latitud,
            longitudOrigen = origen.longitud,
            latitudDestino = destino.latitud,
            longitudDestino = destino.longitud,
            numeroVuelo = vuelo,
            pasajeroNombre = pasajero,
            pasajeroTelefono = telefono,
            pasajeroRut = rut,
            conductorId = conductor?.id,
            conductorNombre = conductor?.nombre ?: "",
            estado = estado,
            inicioMillis = inicio,
            terminoMillis = termino
        )
    }

    fun viajes(): List<Viajes> {
        return listOf(
            //Ayer
            viaje("T-1040", -1, "08:10", SCL_T1, HOTEL, "David Hernández", "+56 9 5555 1001",
                EstadoViaje.FINALIZADO, pedro, "LA 801", 1500, 41, rut = "12.345.678-9"),
            viaje("T-1041", -1, "17:30", LAS_CONDES, SCL_T2, "Matías Castro", "+56 9 5555 1002",
                EstadoViaje.FINALIZADO, carla, "", 1100, 35),

            //Hoy
            viaje("T-1043", 0, "06:30", HOTEL, SCL_T2, "Andrés Lira", "+56 9 5555 1003",
                EstadoViaje.FINALIZADO, juan, "", 400, 38),
            viaje("T-1044", 0, "07:50", SCL_T1, HOTEL, "Isabel Mora", "+56 9 5555 1004",
                EstadoViaje.FINALIZADO, carla, "LA 233", 300, 36),
            viaje("T-1045", 0, "09:15", HOTEL, LAS_CONDES, "David Hernández", "+56 9 5555 1005",
                EstadoViaje.EN_CURSO, juan, "", 18, rut = "12.345.678-9"),
            viaje("T-1050", 0, "10:40", TORRE, HOTEL, "Paula Díaz", "+56 9 5555 1006",
                EstadoViaje.EN_CURSO, rodrigo, "", 25),
            viaje("T-1048", 0, "11:20", SCL_T1, TORRE, "Diego Fuentes", "+56 9 7766 5544",
                EstadoViaje.ASIGNADO, juan, "H2 247"),
            viaje("T-1049", 0, "13:30", HOTEL, SCL_T2, "Lucía Paredes", "+56 9 5555 1007",
                EstadoViaje.ASIGNADO, juan, "LA 412"),
            viaje("T-1046", 0, "14:00", HOTEL, SCL_T2, "Ana Soto", "+56 9 5555 1008",
                EstadoViaje.ASIGNADO, carla, "JA 112"),
            viaje("T-1051", 0, "16:00", LAS_CONDES, HOTEL, "Tomás Herrera", "+56 9 5555 1009",
                EstadoViaje.PENDIENTE),
            viaje("T-1047", 0, "18:40", LAS_CONDES, HOTEL, "Felipe Ortiz", "+56 9 5555 1010",
                EstadoViaje.ASIGNADO, pedro),
            viaje("T-1052", 0, "21:45", SCL_T2, HOTEL, "Sofía Lagos", "+56 9 5555 1011",
                EstadoViaje.PENDIENTE, null, "AR 1290"),

            //Mañana
            viaje("T-1053", 1, "08:00", HOTEL, SCL_T1, "Valentina Rojas", "+56 9 5555 1001",
                EstadoViaje.PENDIENTE, null, "LA 802"),
            viaje("T-1054", 1, "10:30", TORRE, LAS_CONDES, "Tomás Herrera", "+56 9 5555 1009",
                EstadoViaje.ASIGNADO, pedro),
            viaje("T-1055", 1, "19:15", SCL_T2, HOTEL, "Matías Castro", "+56 9 5555 1002",
                EstadoViaje.ASIGNADO, juan, "LA 805"),

            //Pasado mañana
            viaje("T-1056", 2, "07:15", HOTEL, SCL_T1, "Camila Reyes", "+56 9 5555 1005",
                EstadoViaje.PENDIENTE, null, "LA 800")
        ).mapIndexed { index, viaje -> viaje.copy(id = index + 1) }
    }


}
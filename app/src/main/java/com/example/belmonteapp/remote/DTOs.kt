package com.example.belmonteapp.remote

import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

//Clases con la misma forma que el JSON del backend (Gson las llena por nombre de campo).
//Lo que puede venir null en el JSON se declara con "?".

//---------- Login (ms-auth) ----------
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val usuario: UsuarioDto
)

data class UsuarioDto(
    val id: Int,
    val email: String,
    val rol: String,            //ADMIN, PASAJERO o CONDUCTOR
    val idReferencia: Int?      //id del pasajero o conductor (null para el admin)
)

//---------- Viajes (ms-viajes) ----------
data class ViajeDto(
    val id: Int,
    val idPasajero: Int,
    val idConductor: Int?,
    val fecha: String,          //"2026-11-26"
    val hora: String,           //"09:15:00"
    val origen: String,
    val destino: String,
    val numeroVuelo: String?,
    val latitudOrigen: Double,
    val longitudOrigen: Double,
    val latitudDestino: Double,
    val longitudDestino: Double,
    val estado: String,
    val inicio: String?,        //"2026-11-26T09:15:00"
    val termino: String?,
    val nombrePasajero: String?,
    val telefonoPasajero: String?,
    val nombreConductor: String?
)

data class NuevoViajeRequest(
    val idPasajero: Int,
    val fecha: String,
    val hora: String,
    val origen: String,
    val destino: String,
    val numeroVuelo: String,
    val latitudOrigen: Double,
    val longitudOrigen: Double,
    val latitudDestino: Double,
    val longitudDestino: Double
)

data class AsignarRequest(
    val idConductor: Int
)

//---------- Conductores (ms-conductores) ----------
data class ConductorDto(
    val id: Int,
    val nombre: String,
    val telefono: String?,
    val patente: String,
    val modeloVehiculo: String
)

//---------- Conversiones: lo que llega del backend -> entidades de Room ----------

//rutPasajero: el backend no guarda RUT, se completa con el de la cuenta (solo pasajero)
fun ViajeDto.aViaje(rutPasajero: String = ""): Viajes {
    return Viajes(
        id = id,
        codigo = "T-${1039 + id}",
        fecha = LocalDate.parse(fecha),
        hora = LocalTime.parse(hora),
        origen = origen,
        destino = destino,
        latitudOrigen = latitudOrigen,
        longitudOrigen = longitudOrigen,
        latitudDestino = latitudDestino,
        longitudDestino = longitudDestino,
        numeroVuelo = numeroVuelo ?: "",
        pasajeroNombre = nombrePasajero ?: "",
        pasajeroTelefono = telefonoPasajero ?: "",
        pasajeroRut = rutPasajero,
        conductorId = idConductor,
        conductorNombre = nombreConductor ?: "",
        estado = EstadoViaje.valueOf(estado),
        inicioMillis = aMillis(inicio),
        terminoMillis = aMillis(termino)
    )
}

//"2026-11-26T09:15:00" -> milisegundos (0 si viene null)
private fun aMillis(fechaHora: String?): Long {
    if (fechaHora == null) return 0
    return LocalDateTime.parse(fechaHora)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}

//rut: el backend no lo tiene, se conserva el que ya estaba guardado en Room
fun ConductorDto.aConductor(rut: String = ""): Conductor {
    return Conductor(
        id = id,
        nombre = nombre,
        vehiculo = modeloVehiculo,
        patente = patente,
        rut = rut,
        telefono = telefono ?: ""
    )
}

//Viaje creado en la app -> cuerpo del POST /viajes
fun Viajes.aNuevoViajeRequest(idPasajero: Int): NuevoViajeRequest {
    return NuevoViajeRequest(
        idPasajero = idPasajero,
        fecha = fecha.toString(),   //"2026-11-26"
        hora = hora.toString(),     //"09:15"
        origen = origen,
        destino = destino,
        numeroVuelo = numeroVuelo,
        latitudOrigen = latitudOrigen,
        longitudOrigen = longitudOrigen,
        latitudDestino = latitudDestino,
        longitudDestino = longitudDestino
    )
}
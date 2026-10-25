package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "viajes")
data class Viajes (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codigo : String,
    val fecha: LocalDate,
    val hora: LocalTime,
    val origen: String,
    val destino: String,
    val latitudOrigen : Double,
    val longitudOrigen : Double,
    val latitudDestino : Double,
    val longitudDestino : Double,
    val numeroVuelo : String = "", //cuando corresponde
    val pasajeroNombre : String,
    val pasajeroTelefono : String = "",
    val pasajeroRut: String,
    val conductorId : Int? = null, //null mientras el viaje esta pendiente
    val conductorNombre : String = "",
    val conductorRut: String = "",
    val estado : EstadoViaje = EstadoViaje.PENDIENTE,
    val inicioMillis : Long = 0, //momento en que el conductor inicia el viaje
    val terminoMillis : Long = 0 //momento en que finaliza el viaje

){

}
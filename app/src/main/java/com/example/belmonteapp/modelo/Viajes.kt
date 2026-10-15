package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "viajes")
data class Viajes (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val origen: String,
    val destino: String,
    val fecha: LocalDate,
    val hora: LocalTime,
    val pasajeroRut: Cuenta,
    val conductorRut: Cuenta
){

}
package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "cuenta")
data class Cuenta (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombres: String,
    val apellidos: String,
    val rut: String,
    val usuario: Usuario,
    val tipo: Tipo = Tipo.PASAJERO

){

}
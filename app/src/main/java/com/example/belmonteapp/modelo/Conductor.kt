package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "conductor")
data class Conductor (
    @PrimaryKey
    val id : Int,
    val nombre : String,
    val vehiculo : String,
    val patente : String
){

}
package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mail: String,
    val pass:  String

){


}
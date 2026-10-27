package com.example.belmonteapp.modelo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "resena",
    indices = [Index(value = ["viajeId"], unique = true)]
)
data class Resena(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val viajeId: Int,
    val conductorId: Int,
    val pasajeroRut: String,
    val estrellas: Int,
    val comentario: String = "",
    val fechaMillis: Long = System.currentTimeMillis()
) {
}
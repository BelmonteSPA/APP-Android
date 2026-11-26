package com.example.belmonteapp.modelo

import androidx.room3.Entity
import androidx.room3.PrimaryKey

//Sesion iniciada contra el backend. Siempre hay como maximo 1 fila (id = 1).
//Si la tabla esta vacia, la app trabaja en modo local (sin servidor).
@Entity(tableName = "sesion")
data class Sesion(
    @PrimaryKey
    val id: Int = 1,
    val token: String,          //JWT que entrega ms-auth
    val email: String,
    val rol: String,            //ADMIN, PASAJERO o CONDUCTOR
    val idReferencia: Int       //id del pasajero o conductor en el backend (0 para el admin)
)
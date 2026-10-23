package com.example.belmonteapp.modelo

enum class EstadoViaje (
    val texto : String
){
    PENDIENTE("Pendiente"),
    ASIGNADO("Asignado"),
    EN_CURSO("En curso"),
    FINALIZADO("Finalizado")
}
package com.example.belmonteapp.database

import androidx.room3.ColumnTypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converters {

    @ColumnTypeConverter
    fun fechaATexto(fecha: LocalDate): String {
        return fecha.toString()          // 2026-10-23
    }

    @ColumnTypeConverter
    fun textoAFecha(texto: String): LocalDate {
        return LocalDate.parse(texto)
    }

    @ColumnTypeConverter
    fun horaATexto(hora: LocalTime): String {
        return hora.toString()           // 11:20
    }

    @ColumnTypeConverter
    fun textoAHora(texto: String): LocalTime {
        return LocalTime.parse(texto)
    }
}
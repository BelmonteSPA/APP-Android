package com.example.belmonteapp.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

//funciones para mostrar fechas y horas en las pantallas
object Fechas{
    private val idioma = Locale.forLanguageTag("es-CL")

    //fecha de hoy
    fun hoy() : LocalDate{
        return LocalDate.now()
    }

    fun tituloDia(fecha : LocalDate) : String {
        val texto = fecha.format(DateTimeFormatter.ofPattern("EEEE d", idioma))
        return if (fecha == LocalDate.now()) "Hoy, $texto" else mayuscula(texto)
    }

    //la hora de un viaje como texto
    fun horaTexto (hora : LocalTime) : String {
        return hora.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    //"Octubre 2026"
    fun mesAnio(fecha: LocalDate): String {
        return mayuscula(fecha.format(DateTimeFormatter.ofPattern("MMMM yyyy", idioma)))
    }

    //Los 7 dias (lunes a domingo) de la semana de la fecha
    fun semanaDe(fecha: LocalDate): List<LocalDate> {
        val lunes = fecha.minusDays((fecha.dayOfWeek.value - 1).toLong())
        return (0L..6L).map { lunes.plusDays(it) }
    }

    //L M M J V S D
    fun inicialDia(fecha: LocalDate): String {
        return "LMMJVSD"[fecha.dayOfWeek.value - 1].toString()
    }

    //Convierte milisegundos en hora: 11:24
    fun horaDeMillis(millis: Long): String {
        if (millis == 0L) return "--:--"
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    //Convierte segundos en 00:18:42
    fun cronometro(segundos: Long): String {
        return String.format(
            Locale.US,
            "%02d:%02d:%02d",
            segundos / 3600,
            (segundos % 3600) / 60,
            segundos % 60
        )
    }

    private fun mayuscula(texto: String): String {
        return texto.replaceFirstChar { it.uppercase() }
    }
}
package com.example.belmonteapp.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

//abre google maps o waze con las coordenadas indicadas
fun abrirNavegacion (
    context: Context,
    latitud : Double,
    longitud : Double,
    nombre : String
){
    val uri = Uri.parse(
        "geo:$latitud, $longitud?q=$latitud, $longitud(${Uri.encode(nombre)})"
    )
    val intent = Intent(Intent.ACTION_VIEW, uri)
    try {
        context.startActivity(Intent.createChooser(intent, "Navegar con"))
    } catch (e : ActivityNotFoundException){
        Toast.makeText(
            context,
            "No hay una aplicacion de mapa instalada",
            Toast.LENGTH_SHORT).show()
    }
}

//abre el marcador del telefono con el numero del pasajero
fun llamarPasajero(context: Context, telefono : String){
    val numero = telefono.filter { it.isDigit() || it == '+' }
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero"))
    try {
        context.startActivity(intent)
    } catch (e : ActivityNotFoundException){
        Toast.makeText(
            context,
            "No se puede realizar la llamada",
            Toast.LENGTH_SHORT).show()
    }
}

//se muestra en letra grande
fun nombreLugar (lugar : String) : String {
    return lugar.substringBefore(",")
}

//se muestra en letra pequeña
fun direccionLugar(lugar : String) : String {
    return lugar.substringAfter(",", "").trim()
}
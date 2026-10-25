package com.example.belmonteapp.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase

private val RojoClaro = Color(0xFFFDECEA)
private val RojoOscuro = Color(0xFF7A0A1C)
private val GrisClaro = Color(0xFFEDEDED)

//etiqueta con el estado del viaje, cada estado tiene su propio color
@Composable
fun EtiquetaEstado(
    estado : EstadoViaje,
    modifier: Modifier = Modifier
) {
    //fondo, letra y borde
    val colores = when (estado) {
        EstadoViaje.PENDIENTE -> Triple(Color.White, RojoBase, RojoBase)
        EstadoViaje.ASIGNADO -> Triple(RojoClaro, RojoOscuro, RojoClaro)
        EstadoViaje.EN_CURSO -> Triple(RojoBase, Color.White, RojoBase)
        EstadoViaje.FINALIZADO -> Triple(GrisClaro, GrisOscuro, GrisClaro)
    }
    val (fondo, letra, borde) = colores

    Box(
        modifier = modifier
            .background(fondo, RoundedCornerShape(6.dp))
            .border(1.dp, borde, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ){
        Text(
            text = estado.texto.uppercase(),
            color = letra,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}
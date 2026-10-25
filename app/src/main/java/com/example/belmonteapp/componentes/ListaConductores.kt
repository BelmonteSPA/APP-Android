package com.example.belmonteapp.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.Fechas
import java.time.LocalDate

//Lista de conductores para elegir uno
//- Un conductor con un viaje EN_CURSO aparece bloqueado
//- "fecha" es el dia del viaje, para contar cuantos viajes tiene ese dia
@Composable
fun ListaConductores(
    conductores: List<Conductor>,
    viajes: List<Viajes>,
    fecha: LocalDate,
    seleccionado: Conductor?,
    conductorActualId: Int?,
    onSeleccion: (Conductor) -> Unit
) {
    Column {
        conductores.forEach { conductor ->
            val viajeEnCurso = viajes.find {
                it.conductorId == conductor.id && it.estado == EstadoViaje.EN_CURSO
            }
            val cantidad = viajes.count {
                it.conductorId == conductor.id && it.fecha == fecha
            }
            val habilitado = viajeEnCurso == null

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = habilitado) { onSeleccion(conductor) }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                RadioButton(
                    selected = seleccionado?.id == conductor.id,
                    onClick = null,
                    enabled = habilitado,
                    colors = RadioButtonDefaults.colors(selectedColor = RojoBase)
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = if (conductor.id == conductorActualId) {
                            "${conductor.nombre} · actual"
                        } else {
                            conductor.nombre
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (habilitado) Color.Black else Gris
                    )
                    Text(
                        text = "${conductor.vehiculo} · ${conductor.patente}",
                        fontSize = 13.sp,
                        color = if (habilitado) GrisOscuro else Gris
                    )
                }
                Text(
                    text = if (viajeEnCurso == null) {
                        "$cantidad viajes"
                    } else {
                        "En viaje desde ${Fechas.horaDeMillis(viajeEnCurso.inicioMillis)}"
                    },
                    fontSize = 12.sp,
                    color = Gris
                )
            }
            HorizontalDivider(color = Color.LightGray)
        }
    }
}
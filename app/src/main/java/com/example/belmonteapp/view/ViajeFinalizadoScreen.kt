package com.example.belmonteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.util.nombreLugar
import com.example.belmonteapp.viewModel.ViajesViewModel

//Pantalla 14 - Viaje finalizado: resumen y siguiente viaje del dia
@Composable
fun ViajeFinalizadoScreen(
    navController: NavController,
    viajeId: Int,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val scroll = rememberScrollState()

    val viaje = viajes.find { it.id == viajeId } ?: return

    //Duracion en minutos (si el viaje aun no tiene hora de termino se muestra un guion)
    val duracion = if (viaje.inicioMillis > 0 && viaje.terminoMillis > 0) {
        "${(viaje.terminoMillis - viaje.inicioMillis) / 60000} min"
    } else {
        "-"
    }

    //Siguiente viaje del mismo conductor, ese mismo dia, mas tarde y ya asignado
    val siguiente = viajes
        .filter {
            it.conductorId == viaje.conductorId &&
                    it.fecha == viaje.fecha &&
                    it.estado == EstadoViaje.ASIGNADO &&
                    it.hora > viaje.hora
        }
        .minByOrNull { it.hora }

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.VIAJES_DIA) {
                            popUpTo(Routes.VIAJES_DIA) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                ) {
                    Text(
                        text = "Volver a mis viajes",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scroll)
        ) {
            //Encabezado con el check
            CabeceraRoja(alto = 300.dp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(start = 24.dp, top = 56.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.White, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Finalizado",
                            tint = RojoBase,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Viaje finalizado",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "#${viaje.codigo} · FINALIZADO",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        letterSpacing = 2.sp
                    )
                }
            }

            //Inicio, termino y duracion
            TarjetaBlanca(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-44).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Dato("Inicio", Fechas.horaDeMillis(viaje.inicioMillis))
                        Dato("Término", Fechas.horaDeMillis(viaje.terminoMillis))
                        Dato("Duración", duracion)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.LightGray
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Pasajero", color = Gris)
                        Text(
                            text = viaje.pasajeroNombre,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Ruta", color = Gris)
                        Text(
                            text = "${nombreLugar(viaje.origen)} → ${nombreLugar(viaje.destino)}",
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }
                }
            }

            //Siguiente viaje (solo si existe)
            if (siguiente != null) {
                TarjetaBlanca(
                    radio = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .offset(y = (-24).dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Fechas.horaTexto(siguiente.hora),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "SIGUIENTE",
                                fontSize = 11.sp,
                                color = RojoBase,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "${nombreLugar(siguiente.origen)} → ${nombreLugar(siguiente.destino)}",
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

//Un dato con su titulo arriba (Inicio, Término, Duración)
@Composable
private fun Dato(titulo: String, valor: String) {
    Column {
        Text(text = titulo, fontSize = 13.sp, color = Gris)
        Text(
            text = valor,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
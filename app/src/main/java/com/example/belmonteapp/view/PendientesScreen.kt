package com.example.belmonteapp.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.BarraInferior
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.OpcionBarra
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.util.nombreLugar
import com.example.belmonteapp.viewModel.ViajesViewModel

//Pantalla 06 - Pendientes (administrador): viajes que todavia no tienen conductor
@Composable
fun PendientesScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()

    //Solo los PENDIENTE (ya vienen ordenados por fecha y hora desde el DAO)
    val pendientes = viajes.filter { it.estado == EstadoViaje.PENDIENTE }
    val paraHoy = pendientes.count { it.fecha == Fechas.hoy() }

    val opciones = listOf(
        OpcionBarra("Pendientes", Icons.Default.Schedule),
        OpcionBarra("Viajes", Icons.Default.DateRange),
        OpcionBarra("Perfil", Icons.Default.Person)
    )

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            BarraInferior(
                opciones = opciones,
                seleccionada = 0,
                onSeleccion = { indice ->
                    when (indice) {
                        1 -> navController.navigate(Routes.VIAJES_FECHA) {
                            popUpTo(Routes.VIAJES_FECHA) { inclusive = true }
                        }
                        2 -> navController.navigate(Routes.ADMIN)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            //Encabezado rojo con los contadores encima
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CabeceraRoja(alto = 200.dp) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(start = 24.dp, top = 32.dp)
                        ) {
                            Text(
                                text = "SOLICITUDES",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Pendientes",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    TarjetaBlanca(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .offset(y = 44.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            ContadorPendientes(
                                numero = pendientes.size,
                                texto = "Sin asignar",
                                resaltado = true,
                                modifier = Modifier.weight(1f)
                            )
                            ContadorPendientes(
                                numero = paraHoy,
                                texto = "Para hoy",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            //Espacio para la parte de la tarjeta que sobresale del encabezado
            item { Spacer(modifier = Modifier.height(36.dp)) }

            if (pendientes.isEmpty()) {
                item {
                    Text(
                        text = "No hay viajes pendientes",
                        color = Gris,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }

            items(pendientes, key = { it.id }) { viaje ->
                TarjetaPendiente(
                    viaje = viaje,
                    onClick = { navController.navigate(Routes.asignarConductor(viaje.id)) }
                )
            }
        }
    }
}

//Numero grande con su texto abajo (Sin asignar, Para hoy)
@Composable
private fun ContadorPendientes(
    numero: Int,
    texto: String,
    modifier: Modifier = Modifier,
    resaltado: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = numero.toString(),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (resaltado) RojoBase else Color.Black
        )
        Text(
            text = texto,
            fontSize = 13.sp,
            color = GrisOscuro
        )
    }
}

//Una tarjeta de la lista: dia y hora, pasajero, ruta y vuelo
@Composable
private fun TarjetaPendiente(
    viaje: Viajes,
    onClick: () -> Unit
) {
    TarjetaBlanca(
        radio = 20.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = Fechas.horaTexto(viaje.hora),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = Fechas.tituloDia(viaje.fecha),
                    fontSize = 11.sp,
                    color = RojoBase,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = viaje.pasajeroNombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "${nombreLugar(viaje.origen)} → ${nombreLugar(viaje.destino)}",
                    fontSize = 13.sp,
                    color = GrisOscuro
                )
                if (viaje.numeroVuelo.isNotEmpty()) {
                    Text(
                        text = "Vuelo ${viaje.numeroVuelo}",
                        fontSize = 13.sp,
                        color = Gris
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Asignar conductor",
                tint = Gris
            )
        }
    }
}
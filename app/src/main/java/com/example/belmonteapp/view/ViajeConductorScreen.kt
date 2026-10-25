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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Today
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
import com.example.belmonteapp.componentes.EtiquetaEstado
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

//Pantalla 10 - Viajes del día (soloHoy = true) y pestaña "Asignados" (soloHoy = false)
@Composable
fun ViajesConductorScreen(
    navController: NavController,
    soloHoy: Boolean,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val conductores by viajesViewModel.conductores.collectAsState()

    val conductorId = viajesViewModel.conductorId
    val conductor = conductores.find { it.id == conductorId }
    val hoy = Fechas.hoy()

    //Solo los viajes de este conductor
    val misViajes = viajes.filter { it.conductorId == conductorId }
    val base = if (soloHoy) misViajes.filter { it.fecha == hoy } else misViajes
    val lista = if (soloHoy) {
        base
    } else {
        base.filter { it.estado == EstadoViaje.ASIGNADO || it.estado == EstadoViaje.EN_CURSO }
    }

    val opciones = listOf(
        OpcionBarra("Hoy", Icons.Default.Today),
        OpcionBarra("Asignados", Icons.Default.CheckCircle),
        OpcionBarra("Perfil", Icons.Default.Person)
    )

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            BarraInferior(
                opciones = opciones,
                seleccionada = if (soloHoy) 0 else 1,
                onSeleccion = { indice ->
                    when (indice) {
                        0 -> if (!soloHoy) navController.navigate(Routes.VIAJES_DIA)
                        1 -> if (soloHoy) navController.navigate(Routes.VIAJES_ASIGNADOS)
                        2 -> navController.navigate(Routes.CONDUCTOR)
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
                    CabeceraRoja(alto = 220.dp) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(start = 24.dp, top = 32.dp)
                        ) {
                            Text(
                                text = "${conductor?.nombre ?: "Conductor"} · ${conductor?.patente ?: ""}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (soloHoy) Fechas.tituloDia(hoy) else "Asignados",
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
                            Contador(
                                numero = base.count { it.estado == EstadoViaje.ASIGNADO },
                                texto = "Asignados",
                                modifier = Modifier.weight(1f)
                            )
                            Contador(
                                numero = base.count { it.estado == EstadoViaje.EN_CURSO },
                                texto = "En curso",
                                resaltado = true,
                                modifier = Modifier.weight(1f)
                            )
                            Contador(
                                numero = base.count { it.estado == EstadoViaje.FINALIZADO },
                                texto = "Finalizados",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            //Espacio para la parte de la tarjeta que sobresale del encabezado
            item { Spacer(modifier = Modifier.height(36.dp)) }

            if (lista.isEmpty()) {
                item {
                    Text(
                        text = "No tienes viajes para mostrar",
                        color = Gris,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }

            items(lista, key = { it.id }) { viaje ->
                TarjetaViajeConductor(
                    viaje = viaje,
                    onClick = {
                        //Cada estado lleva a su pantalla
                        when (viaje.estado) {
                            EstadoViaje.ASIGNADO -> navController.navigate(Routes.detalleViaje(viaje.id))
                            EstadoViaje.EN_CURSO -> navController.navigate(Routes.viajeEnCurso(viaje.id))
                            else -> navController.navigate(Routes.viajeFinalizado(viaje.id))
                        }
                    }
                )
            }
        }
    }
}

//Numero grande con su texto abajo (Asignados, En curso, Finalizados)
@Composable
private fun Contador(
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

//Una tarjeta de la lista: hora, origen, destino y pasajero
@Composable
private fun TarjetaViajeConductor(
    viaje: Viajes,
    onClick: () -> Unit
) {
    val finalizado = viaje.estado == EstadoViaje.FINALIZADO

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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (finalizado) Gris else Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                EtiquetaEstado(estado = viaje.estado)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = nombreLugar(viaje.origen),
                    fontSize = 13.sp,
                    color = Gris
                )
                Text(
                    text = nombreLugar(viaje.destino),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (finalizado) Gris else Color.Black
                )
                Text(
                    text = if (viaje.numeroVuelo.isEmpty()) {
                        viaje.pasajeroNombre
                    } else {
                        "${viaje.pasajeroNombre} · ${viaje.numeroVuelo}"
                    },
                    fontSize = 13.sp,
                    color = Gris
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver viaje",
                tint = Gris
            )
        }
    }
}
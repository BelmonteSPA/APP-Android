package com.example.belmonteapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.belmonteapp.viewModel.ViajesViewModel
import java.time.LocalDate

//Pantalla 08 - Viajes por fecha (administrador): semana, filtro por estado y lista
@Composable
fun ViajesFechaScreen(
    navController: NavController,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()

    //Dia elegido en la semana y estado elegido en el filtro (null = Todos)
    var fechaSeleccionada by remember { mutableStateOf(LocalDate.now()) }
    var filtro by remember { mutableStateOf<EstadoViaje?>(null) }

    val delDia = viajes.filter { it.fecha == fechaSeleccionada }
    val lista = if (filtro == null) delDia else delDia.filter { it.estado == filtro }

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
                seleccionada = 1,
                onSeleccion = { indice ->
                    when (indice) {
                        0 -> navController.navigate(Routes.PENDIENTES)
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
            //Encabezado rojo con la semana encima
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
                                text = Fechas.mesAnio(fechaSeleccionada),
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 14.sp,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Viajes",
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
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Fechas.semanaDe(fechaSeleccionada).forEach { dia ->
                                DiaSemana(
                                    dia = dia,
                                    seleccionado = dia == fechaSeleccionada,
                                    onClick = { fechaSeleccionada = dia }
                                )
                            }
                        }
                    }
                }
            }

            //Espacio para la parte de la tarjeta que sobresale del encabezado
            item { Spacer(modifier = Modifier.height(36.dp)) }

            //Filtro por estado, con la cantidad de viajes de ese dia
            item {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FiltroEstado(
                        texto = "Todos",
                        cantidad = delDia.size,
                        activo = filtro == null,
                        onClick = { filtro = null }
                    )
                    EstadoViaje.entries.forEach { estado ->
                        FiltroEstado(
                            texto = estado.texto,
                            cantidad = delDia.count { it.estado == estado },
                            activo = filtro == estado,
                            onClick = { filtro = estado }
                        )
                    }
                }
            }

            if (lista.isEmpty()) {
                item {
                    Text(
                        text = "No hay viajes para este día",
                        color = Gris,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    )
                }
            }

            items(lista, key = { it.id }) { viaje ->
                TarjetaViajeAdmin(
                    viaje = viaje,
                    onClick = {
                        //PENDIENTE se asigna; ASIGNADO se puede cambiar de conductor
                        when (viaje.estado) {
                            EstadoViaje.PENDIENTE -> navController.navigate(Routes.asignarConductor(viaje.id))
                            EstadoViaje.ASIGNADO -> navController.navigate(Routes.modificarAsignacion(viaje.id))
                            else -> {}
                        }
                    }
                )
            }
        }
    }
}

//Un dia de la semana: inicial arriba y numero abajo (rojo si esta elegido)
@Composable
private fun DiaSemana(
    dia: LocalDate,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = if (seleccionado) RojoBase else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Text(
            text = Fechas.inicialDia(dia),
            fontSize = 12.sp,
            color = if (seleccionado) Color.White else Gris
        )
        Text(
            text = dia.dayOfMonth.toString(),
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (seleccionado) Color.White else Color.Black
        )
    }
}

//Boton del filtro: "Pendiente 1"
@Composable
private fun FiltroEstado(
    texto: String,
    cantidad: Int,
    activo: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (activo) RojoBase else Color.LightGray),
        colors = CardDefaults.cardColors(
            containerColor = if (activo) RojoBase else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .width(96.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = cantidad.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (activo) Color.White else Color.Black
            )
            Text(
                text = texto,
                fontSize = 12.sp,
                color = if (activo) Color.White else GrisOscuro
            )
        }
    }
}

//Una tarjeta de la lista: hora, pasajero, conductor y estado
@Composable
private fun TarjetaViajeAdmin(
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
            Text(
                text = Fechas.horaTexto(viaje.hora),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
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
                    text = if (viaje.conductorNombre.isEmpty()) "Sin asignar" else viaje.conductorNombre,
                    fontSize = 13.sp,
                    color = Gris
                )
            }
            EtiquetaEstado(estado = viaje.estado)
        }
    }
}
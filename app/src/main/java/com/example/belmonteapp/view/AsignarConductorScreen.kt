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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.EtiquetaEstado
import com.example.belmonteapp.componentes.ListaConductores
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.Conductor
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.util.nombreLugar
import com.example.belmonteapp.viewModel.ViajesViewModel

//Pantalla 07 - Asignar conductor a un viaje PENDIENTE
@Composable
fun AsignarConductorScreen(
    navController: NavController,
    viajeId: Int,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val conductores by viajesViewModel.conductores.collectAsState()
    val scroll = rememberScrollState()

    //Conductor elegido en la lista (null = todavia ninguno)
    var seleccionado by remember { mutableStateOf<Conductor?>(null) }

    val viaje = viajes.find { it.id == viajeId } ?: return

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
                        seleccionado?.let { conductor ->
                            viajesViewModel.asignarConductor(viaje, conductor)
                            navController.popBackStack()
                        }
                    },
                    enabled = seleccionado != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                ) {
                    Text(
                        text = if (seleccionado == null) {
                            "Elige un conductor"
                        } else {
                            "Asignar a ${seleccionado?.nombre}"
                        },
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
            CabeceraRoja(
                alto = 160.dp,
                onVolver = { navController.popBackStack() }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 56.dp)
                ) {
                    Text(
                        text = "ASIGNAR CONDUCTOR",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = "Viaje #${viaje.codigo}",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            //Resumen del viaje
            TarjetaBlanca(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-44).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    FilaDato("Pasajero", viaje.pasajeroNombre)
                    FilaDato("Fecha · hora", "${Fechas.tituloDia(viaje.fecha)} · ${Fechas.horaTexto(viaje.hora)}")
                    FilaDato("Origen", nombreLugar(viaje.origen))
                    FilaDato("Destino", nombreLugar(viaje.destino))
                    if (viaje.numeroVuelo.isNotEmpty()) {
                        FilaDato("Vuelo", viaje.numeroVuelo)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Estado", color = Gris, fontSize = 14.sp)
                        EtiquetaEstado(estado = viaje.estado)
                    }
                }
            }

            //Lista de conductores para elegir
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .offset(y = (-24).dp)
            ) {
                Text(
                    text = "CONDUCTORES DISPONIBLES",
                    fontSize = 12.sp,
                    letterSpacing = 4.sp,
                    color = Gris
                )
                Spacer(modifier = Modifier.height(8.dp))
                ListaConductores(
                    conductores = conductores,
                    viajes = viajes,
                    fecha = viaje.fecha,
                    seleccionado = seleccionado,
                    conductorActualId = viaje.conductorId,
                    onSeleccion = { seleccionado = it }
                )
            }
        }
    }
}

//Fila con titulo a la izquierda y valor a la derecha
@Composable
private fun FilaDato(titulo: String, valor: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = titulo, color = Gris, fontSize = 14.sp)
        Text(
            text = valor,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 24.dp)
        )
    }
}
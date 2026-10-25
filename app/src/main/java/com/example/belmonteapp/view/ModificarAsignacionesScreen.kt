package com.example.belmonteapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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

//Pantalla 09 - Modificar asignación: detalle del viaje y, encima, el panel para cambiar de conductor
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarAsignacionScreen(
    navController: NavController,
    viajeId: Int,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val conductores by viajesViewModel.conductores.collectAsState()
    val scroll = rememberScrollState()
    val estadoPanel = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    //Conductor elegido en el panel (null = todavia ninguno)
    var seleccionado by remember { mutableStateOf<Conductor?>(null) }

    val viaje = viajes.find { it.id == viajeId } ?: return
    val conductorActual = conductores.find { it.id == viaje.conductorId }

    //Detalle del viaje (queda de fondo)
    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scroll)
        ) {
            CabeceraRoja(
                alto = 240.dp,
                onVolver = { navController.popBackStack() }
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, end = 24.dp, bottom = 56.dp)
                ) {
                    EtiquetaEstado(estado = viaje.estado)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${Fechas.tituloDia(viaje.fecha)} · ${Fechas.horaTexto(viaje.hora)}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "${nombreLugar(viaje.origen)} → ${nombreLugar(viaje.destino)}",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }

            TarjetaBlanca(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-44).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Viaje #${viaje.codigo}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (viaje.numeroVuelo.isEmpty()) {
                            viaje.pasajeroNombre
                        } else {
                            "${viaje.pasajeroNombre} · Vuelo ${viaje.numeroVuelo}"
                        },
                        fontSize = 14.sp,
                        color = Gris
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Conductor", color = Gris)
                        Text(
                            text = viaje.conductorNombre,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Vehículo", color = Gris)
                        Text(
                            text = if (conductorActual == null) {
                                "-"
                            } else {
                                "${conductorActual.vehiculo} · ${conductorActual.patente}"
                            },
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }

    //Panel que sube desde abajo para elegir el nuevo conductor
    ModalBottomSheet(
        onDismissRequest = { navController.popBackStack() },
        sheetState = estadoPanel,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Cambiar conductor",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "El viaje seguirá en estado ${viaje.estado.texto.uppercase()}.",
                fontSize = 14.sp,
                color = Gris,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )

            ListaConductores(
                conductores = conductores,
                viajes = viajes,
                fecha = viaje.fecha,
                seleccionado = seleccionado,
                conductorActualId = viaje.conductorId,
                onSeleccion = { seleccionado = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text(text = "Cancelar", fontWeight = FontWeight.ExtraBold)
                }
                Button(
                    onClick = {
                        seleccionado?.let { conductor ->
                            viajesViewModel.asignarConductor(viaje, conductor)
                            navController.popBackStack()
                        }
                    },
                    //Solo se puede confirmar si se eligio un conductor distinto al actual
                    enabled = seleccionado != null && seleccionado?.id != viaje.conductorId,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                ) {
                    Text(
                        text = "Confirmar cambio",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
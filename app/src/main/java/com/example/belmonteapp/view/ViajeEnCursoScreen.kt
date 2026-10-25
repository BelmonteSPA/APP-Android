package com.example.belmonteapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.util.abrirNavegacion
import com.example.belmonteapp.util.direccionLugar
import com.example.belmonteapp.util.llamarPasajero
import com.example.belmonteapp.util.nombreLugar
import com.example.belmonteapp.viewModel.ViajesViewModel
import kotlinx.coroutines.delay

//Pantalla 12 - Viaje en curso: cronometro, destino, navegar y finalizar
@Composable
fun ViajeEnCursoScreen(
    navController: NavController,
    viajeId: Int,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val contexto = LocalContext.current
    val scroll = rememberScrollState()

    //"ahora" se actualiza cada segundo y hace avanzar el cronometro
    var ahora by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            ahora = System.currentTimeMillis()
        }
    }

    val viaje = viajes.find { it.id == viajeId } ?: return

    val segundos = if (viaje.inicioMillis > 0) (ahora - viaje.inicioMillis) / 1000 else 0L

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        abrirNavegacion(
                            contexto,
                            viaje.latitudDestino,
                            viaje.longitudDestino,
                            nombreLugar(viaje.destino)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                ) {
                    Icon(
                        imageVector = Icons.Default.Navigation,
                        contentDescription = "Navegar",
                        tint = Color.White
                    )
                    Text(
                        text = "  Navegar al destino",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = {
                        viajesViewModel.finalizarViaje(viaje)
                        navController.navigate(Routes.viajeFinalizado(viaje.id)) {
                            popUpTo(Routes.VIAJE_EN_CURSO) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, RojoBase),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoBase)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Finalizar"
                    )
                    Text(
                        text = "  Finalizar viaje",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                TextButton(
                    onClick = { llamarPasajero(contexto, viaje.pasajeroTelefono) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Llamar al pasajero · ${viaje.pasajeroTelefono}",
                        color = Gris
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
            //Encabezado: volver, estado, cronometro y destino
            CabeceraRoja(
                alto = 360.dp,
                onVolver = { navController.popBackStack() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 68.dp, bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "EN CURSO · #${viaje.codigo}",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = Fechas.cronometro(segundos),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Rumbo a",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                    Text(
                        text = nombreLugar(viaje.destino),
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = direccionLugar(viaje.destino),
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            //Pasajero e inicio del viaje
            TarjetaBlanca(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-44).dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Pasajero", fontSize = 13.sp, color = Gris)
                        Text(
                            text = viaje.pasajeroNombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Column {
                        Text(text = "Inicio", fontSize = 13.sp, color = Gris)
                        Text(
                            text = Fechas.horaDeMillis(viaje.inicioMillis),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            //Recorrido: el origen ya se recogio, falta llegar al destino
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .offset(y = (-24).dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .border(2.dp, Gris)
                    )
                    Text(
                        text = "  ${nombreLugar(viaje.origen)} · recogido ${Fechas.horaDeMillis(viaje.inicioMillis)}",
                        color = Gris,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(RojoBase)
                    )
                    Text(
                        text = "  ${nombreLugar(viaje.destino)}",
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
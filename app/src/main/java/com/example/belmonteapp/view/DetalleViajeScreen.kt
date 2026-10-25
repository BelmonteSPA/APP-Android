package com.example.belmonteapp.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.EtiquetaEstado
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.abrirNavegacion
import com.example.belmonteapp.util.direccionLugar
import com.example.belmonteapp.util.nombreLugar
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.viewModel.ViajesViewModel

//Pantalla 11 - Detalle del viaje (ASIGNADO): origen, destino y botón para iniciar
@Composable
fun DetalleViajeScreen(
    navController: NavController,
    viajeId: Int,
    viajesViewModel: ViajesViewModel = viewModel()
) {
    val viajes by viajesViewModel.viajes.collectAsState()
    val contexto = LocalContext.current
    val scroll = rememberScrollState()

    //Busca el viaje por su id (mientras carga la base de datos no hay nada que mostrar)
    val viaje = viajes.find { it.id == viajeId } ?: return

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = {
            //El boton solo aparece si el viaje esta ASIGNADO
            if (viaje.estado == EstadoViaje.ASIGNADO) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .navigationBarsPadding()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = {
                            viajesViewModel.iniciarViaje(viaje)
                            navController.navigate(Routes.viajeEnCurso(viaje.id)) {
                                popUpTo(Routes.DETALLE_VIAJE) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                    ) {
                        Text(
                            text = "Iniciar viaje",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
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
            //Encabezado: codigo del viaje, estado, hora y pasajero
            CabeceraRoja(
                alto = 260.dp,
                onVolver = { navController.popBackStack() }
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(top = 20.dp, end = 24.dp)
                ) {
                    EtiquetaEstado(estado = viaje.estado)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 56.dp)
                ) {
                    Text(
                        text = "#${viaje.codigo}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        letterSpacing = 4.sp
                    )
                    Text(
                        text = Fechas.horaTexto(viaje.hora),
                        color = Color.White,
                        fontSize = 56.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (viaje.numeroVuelo.isEmpty()) {
                            viaje.pasajeroNombre
                        } else {
                            "${viaje.pasajeroNombre} · Vuelo ${viaje.numeroVuelo}"
                        },
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            //Tarjeta con origen y destino (se monta sobre el encabezado)
            TarjetaBlanca(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-44).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "ORIGEN",
                        fontSize = 12.sp,
                        letterSpacing = 4.sp,
                        color = Gris
                    )
                    Text(
                        text = nombreLugar(viaje.origen),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = direccionLugar(viaje.origen),
                        fontSize = 14.sp,
                        color = GrisOscuro
                    )
                    Text(
                        text = "${viaje.latitudOrigen}, ${viaje.longitudOrigen}",
                        fontSize = 12.sp,
                        color = Gris
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            abrirNavegacion(
                                contexto,
                                viaje.latitudOrigen,
                                viaje.longitudOrigen,
                                nombreLugar(viaje.origen)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RojoBase),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoBase)
                    ) {
                        Text(text = "Navegar al origen")
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 20.dp),
                        color = Color.LightGray
                    )

                    Text(
                        text = "DESTINO",
                        fontSize = 12.sp,
                        letterSpacing = 4.sp,
                        color = Gris
                    )
                    Text(
                        text = nombreLugar(viaje.destino),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = direccionLugar(viaje.destino),
                        fontSize = 14.sp,
                        color = GrisOscuro
                    )
                    Text(
                        text = "${viaje.latitudDestino}, ${viaje.longitudDestino}",
                        fontSize = 12.sp,
                        color = Gris
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
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
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RojoBase),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoBase)
                    ) {
                        Text(text = "Navegar al destino")
                    }
                }
            }
        }
    }
}


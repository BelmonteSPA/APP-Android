package com.example.belmonteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.belmonteapp.componentes.coloresCampo
import com.example.belmonteapp.modelo.Viajes
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.viewModel.MisViajesViewModel
import com.example.belmonteapp.viewModel.NuevoViajeViewModel
import com.example.belmonteapp.viewModel.ViajesViewModel

@Composable
fun MisViajesScreen(
    navController: NavController,
    nuevoViajeViewModel: NuevoViajeViewModel = viewModel(),
    viajesViewModel: ViajesViewModel = viewModel(),
    viewModel: MisViajesViewModel = viewModel()
) {
    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
    val listaViajes by viewModel.viajesFiltrados.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // 1. Encabezado con buscador
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                CabeceraRoja(
                    alto = 200.dp
                ) {
                    Text(
                        text = "Mis Viajes",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .statusBarsPadding()
                            .padding(start = 20.dp, top = 20.dp)
                    )
                }

                TarjetaBlanca(
                    radio = 20.dp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .offset(y = 44.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = textoBusqueda,
                            onValueChange = { nuevoTexto -> viewModel.onTextoBusquedaChange(nuevoTexto) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar por origen, destino, pasajero...", color = GrisOscuro) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.Black)
                            },
                            trailingIcon = {
                                if (textoBusqueda.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onTextoBusquedaChange("") }) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Black)
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = coloresCampo()
                        )
                    }
                }
            }
        }

        // Espacio para la tarjeta del buscador que sobresale del encabezado
        item {
            Spacer(modifier = Modifier.height(36.dp))
        }

        // 2. Resultado de la lista
        if (listaViajes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontraron viajes", color = GrisOscuro)
                }
            }
        } else {
            items(listaViajes, key = { if (it.id != 0) it.id else it.codigo }) { viaje ->
                Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                    TarjetaViajeItem(viaje = viaje)
                }
            }
        }
    }
}

@Composable
fun TarjetaViajeItem(viaje: Viajes) {
    TarjetaBlanca(
        radio = 20.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viaje.codigo,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Text(
                    text = viaje.estado.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF666666)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pasajero: ${viaje.pasajeroNombre}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "De: ${viaje.origen}",
                fontSize = 13.sp,
                color = GrisOscuro
            )
            Text(
                text = "A: ${viaje.destino}",
                fontSize = 13.sp,
                color = GrisOscuro
            )
        }
    }
}

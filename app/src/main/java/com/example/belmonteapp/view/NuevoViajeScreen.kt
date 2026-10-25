package com.ejemplo.miapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.viewModel.MenuViewModel
import com.example.belmonteapp.viewModel.NuevoViajeViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun NuevoViajeScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = viewModel(),
    nuevoViajeViewModel: NuevoViajeViewModel = viewModel()
) {
    var origen by remember { mutableStateOf(false) }
    var destino by remember { mutableStateOf(false) }

    // Box permite superponer elementos (como capas)
    Box(modifier = Modifier.fillMaxSize()) {

        // CAPA 1 (Fondo): El Mapa
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                Configuration.getInstance().userAgentValue = "BelmonteApp/1.0 (tucorreo@gmail.com)"

                MapView(context).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    // Centrar mapa (Ejemplo: Santiago)
                    val ubicacionActual = GeoPoint(-33.4489, -70.6693)
                    controller.setZoom(16.0)
                    controller.setCenter(ubicacionActual)

                    // Marcador de la ubicación del usuario (ej: auto o usuario)
                    val marker = Marker(this)
                    marker.position = ubicacionActual
                    marker.title = "Tu ubicación"
                    overlays.add(marker)
                }
            }
        )

        // CAPA 2 (Arriba): Tarjeta de ingreso de direcciones estilo Uber
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .statusBarsPadding()
                .padding(25.dp)
                .align(Alignment.TopCenter), // Lo posiciona arriba en la pantalla
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nuevoViajeViewModel.origen,
                    onValueChange = { nuevoViajeViewModel.agregarOrigen(it)},
                    label = {
                        Text("Punto de partida")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = nuevoViajeViewModel.destino,
                    onValueChange = { nuevoViajeViewModel.agregarDestino(it) },
                    label = {
                        Text("¿A dónde vas?")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth()
                .padding(start = 300.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los textos a los extremos
        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.HOME)
                },
                modifier = Modifier
                    .padding(end = 24.dp, bottom = 24.dp)
                    .offset(y = (-150).dp)
                    .size(44.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Cancelar",
                    tint = RojoBase
                )
            }
            IconButton(
                onClick = {
                    navController.navigate(Routes.HOME)
                },
                modifier = Modifier
                    .padding(end = 24.dp, bottom = 24.dp)
                    .offset(y = (-150).dp)
                    .size(44.dp)
                    .background(RojoBase, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirmar",
                    tint = Color.White
                )
            }

        }


    }
}
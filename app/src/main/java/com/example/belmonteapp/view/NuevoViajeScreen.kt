package com.ejemplo.miapp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.events.MapEventsReceiver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.modelo.EstadoSeleccion
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
                    val eventosMapa = MapEventsOverlay(
                        object : MapEventsReceiver {

                            override fun singleTapConfirmedHelper(
                                p: GeoPoint?
                            ): Boolean {

                                if (p != null) {

                                    when (nuevoViajeViewModel.estadoSeleccion) {

                                        EstadoSeleccion.SELECCIONAR_ORIGEN -> {
                                            println("CLICK ORIGEN")

                                            nuevoViajeViewModel.establecerOrigen(context,p)
                                        }
                                        EstadoSeleccion.SELECCIONANDO_DESTINO -> {
                                            nuevoViajeViewModel.establecerDestino(context,p)
                                        }
                                        EstadoSeleccion.LISTO -> {
                                        }
                                    }
                                }

                                return true
                            }

                            override fun longPressHelper(
                                p: GeoPoint?
                            ): Boolean {
                                return false
                            }
                        }
                    )

                    overlays.add(eventosMapa)
                }
            }
        )

        // CAPA 2 (Arriba): Tarjeta de ingreso de direcciones estilo Uber
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
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
                when (nuevoViajeViewModel.estadoSeleccion) {

                    EstadoSeleccion.SELECCIONAR_ORIGEN -> {
                        Text("📍 Selecciona tu punto de origen")
                        Text("Toca cualquier lugar del mapa")
                    }

                    EstadoSeleccion.SELECCIONANDO_DESTINO -> {
                        Text("📍 ${nuevoViajeViewModel.direccionOrigen}")
                        Text("🏁 Selecciona tu punto de destino")
                    }

                    EstadoSeleccion.LISTO -> {

                        Text("📍 ${nuevoViajeViewModel.direccionOrigen}")
                        Text("🏁 ${nuevoViajeViewModel.direccionDestino}")
                    }


                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 300.dp, end = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween, // Separa los textos a los extremos
            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.HOME)


                    },
                    modifier = Modifier
                        .padding(end = 24.dp, bottom = 24.dp)
                        .offset(y = (-50).dp)
                        .wrapContentWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBase
                    )
                ){
                    Text(
                        text = "Cancelar",
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        navController.navigate(Routes.HOME)


                    },
                    modifier = Modifier
                        .padding(end = 24.dp, bottom = 24.dp)
                        .offset(y = (-50).dp)
                        .wrapContentWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBase
                    )
                ){
                    Text(
                        text = "Confirmar",
                        color = Color.White
                    )
                }


            }


        }
    }
}
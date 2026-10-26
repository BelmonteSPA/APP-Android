package com.ejemplo.miapp


import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.events.MapEventsReceiver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.BorderStroke
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.core.content.ContextCompat
import com.example.belmonteapp.R
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import android.widget.Toast
import com.example.belmonteapp.viewModel.ViajesViewModel

@Composable
fun NuevoViajeScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = viewModel(),
    nuevoViajeViewModel: NuevoViajeViewModel = viewModel(),
    viajesViewModel: ViajesViewModel = viewModel()
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val context = LocalContext.current

    // Launcher para pedir el permiso de ubicación
    val launcherPermisos = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { estaConcedido ->
        if (estaConcedido) {
            // El usuario aceptó el permiso
        }
    }

    // Solicitar el permiso al entrar a la pantalla
    LaunchedEffect(Unit) {
        launcherPermisos.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    // Box permite superponer elementos (como capas)
    Box(modifier = Modifier.fillMaxSize()) {

        // CAPA 1 (Fondo): El Mapa
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                Configuration.getInstance().userAgentValue = "BelmonteApp/1.0 (tucorreo@gmail.com)"

                MapView(context).apply {
                    mapView = this
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)

                    // Filtro de escala de grises
                    overlayManager.tilesOverlay.setColorFilter(
                        android.graphics.ColorMatrixColorFilter(
                            android.graphics.ColorMatrix().apply { setSaturation(0.2f) }
                        )
                    )
                    // Punto inicial para que NUNCA parta en pantalla gris
                    val ubicacionInicial = GeoPoint(-33.4489, -70.6693) // Santiago por defecto
                    controller.setZoom(16.0)
                    controller.setCenter(ubicacionInicial)

                    // ubicación REAL
                    val miUbicacionOverlay = MyLocationNewOverlay(
                        GpsMyLocationProvider(context),
                        this
                    ).apply {
                        enableMyLocation()
                        enableFollowLocation()

                    }

                    overlays.add(miUbicacionOverlay)

                    // 3. Zoom inicial
                    controller.setZoom(16.0)

                    val eventosMapa = MapEventsOverlay(
                        object : MapEventsReceiver {

                            override fun singleTapConfirmedHelper(
                                p: GeoPoint?
                            ): Boolean {

                                if (p != null) {

                                    when (nuevoViajeViewModel.estadoSeleccion) {

                                        EstadoSeleccion.SELECCIONAR_ORIGEN -> {

                                            nuevoViajeViewModel.establecerOrigen(context, p)
                                        }

                                        EstadoSeleccion.SELECCIONANDO_DESTINO -> {
                                            nuevoViajeViewModel.establecerDestino(context, p)
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
        //Cambio de marcadores en el mapa (origen o destino)
        LaunchedEffect(
            nuevoViajeViewModel.marcadorOrigen,
            nuevoViajeViewModel.marcadorDestino
        ) {

            mapView?.let { mapa ->

                mapa.overlays.removeAll {
                    it is Marker &&
                            (it.title == "Origen" || it.title == "Destino")
                }

                nuevoViajeViewModel.marcadorOrigen?.let { punto ->

                    val origenMarker = Marker(mapa)

                    origenMarker.position = punto
                    origenMarker.title = "Origen"

                    origenMarker.icon =
                        ContextCompat.getDrawable(
                            mapa.context,
                            R.drawable.ic_origen
                        )
                    origenMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )


                    mapa.overlays.add(origenMarker)
                }

                nuevoViajeViewModel.marcadorDestino?.let { punto ->

                    val destinoMarker = Marker(mapa)

                    destinoMarker.position = punto
                    destinoMarker.title = "Destino"
                    destinoMarker.icon =
                        ContextCompat.getDrawable(
                            mapa.context,
                            R.drawable.ic_destino
                        )
                    destinoMarker.setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )
                    mapa.overlays.add(destinoMarker)
                }

                mapa.invalidate()
            }
        }
        // CAPA 2 (Arriba): Tarjeta de ingreso de direcciones estilo Uber
        Card(
            modifier = Modifier
                .fillMaxWidth()
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

                        Text("📍 ${nuevoViajeViewModel.direccionOrigen}", color = Color.Black)
                        Text("🏁 ${nuevoViajeViewModel.direccionDestino}", color = Color.Black)
                    }

                }
                nuevoViajeViewModel.errorDistancia?.let {

                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }
            if (
                nuevoViajeViewModel.estadoSeleccion == EstadoSeleccion.LISTO
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp) // Separa los textos a los extremos
                ) {
                    Button(
                        onClick = {
                            nuevoViajeViewModel.reiniciarTodo()


                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, RojoBase),
                        colors = ButtonDefaults
                            .outlinedButtonColors(contentColor = RojoBase)
                    ) {
                        Text(
                            text = "Cancelar",
                            color = RojoBase
                        )
                    }
                    Button(
                        onClick = {
                            if (nuevoViajeViewModel.viajeValido()) {
                                nuevoViajeViewModel.construirViaje()?.let { viaje ->
                                    viajesViewModel.crearViaje(viaje) {
                                        Toast.makeText(context, "Viaje solicitado", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        enabled = nuevoViajeViewModel.viajeValido(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RojoBase
                        )
                    ) {
                        Text(
                            text = "Confirmar",
                            color = Color.White
                        )
                    }


                }


            }
        }
    }
}
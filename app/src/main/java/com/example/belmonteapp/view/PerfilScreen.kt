package com.example.belmonteapp.view

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import android.util.Log
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import com.example.belmonteapp.util.Fechas
import com.example.belmonteapp.util.nombreLugar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.componentes.SelectorDesplegable
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.componentes.coloresCampo
import com.example.belmonteapp.data.SesionManager
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.util.abrirReclamoSernac
import com.example.belmonteapp.viewModel.PerfilViewModel
import com.example.belmonteapp.viewModel.QrViewModel
import com.example.belmonteapp.viewModel.RegistroViewModel
import com.example.belmonteapp.viewModel.ViajesViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.example.belmonteapp.modelo.EstadoViaje
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.mutableIntStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    perfilViewModel: PerfilViewModel = viewModel(),
    registroViewModel: RegistroViewModel = viewModel(),
    qrViewModel: QrViewModel = viewModel(),
    viajesViewModel: ViajesViewModel = viewModel()
) {
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) fotoUri = uri }

    var editando by remember { mutableStateOf(false) }
    // Carga los datos de la cuenta que inició sesión
    LaunchedEffect(Unit) { registroViewModel.cargarDesdeSesion() }

    // Datos para el encabezado
    val cuenta = SesionManager.cuentaActual
    val nombreCompleto =
        if (cuenta == null) "Usuario" else "${cuenta.nombres} ${cuenta.apellidos}"

    // Escáner de QR
    val context = LocalContext.current
    val contenidoQR by qrViewModel.contenidoQR.collectAsStateWithLifecycle()
    val opcionesQR = remember {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    val scanner = remember(context, opcionesQR) {
        GmsBarcodeScanning.getClient(context, opcionesQR)
    }
    var mostrarDatos by remember { mutableStateOf(false) }

    // Conductor del vehículo escaneado: se busca por la patente del QR en Room
    val conductores by viajesViewModel.conductores.collectAsState()
    val conductorQR =
        if (contenidoQR.isEmpty()) null else conductores.find { it.patente == qrViewModel.patente() }
    // Conductor del viaje del pasajero: en curso, o si no, el del último viaje
    val viajes by viajesViewModel.viajes.collectAsState()
    val viajeActual = viajesViewModel.viajeConConductor(viajes, cuenta?.rut.orEmpty())
    val conductorViaje = conductores.find { it.id == viajeActual?.conductorId }
    val enCurso = viajeActual?.estado == EstadoViaje.EN_CURSO

    // Aviso del botón Notificación
    var mostrarAviso by remember { mutableStateOf(false) }
    val mensajeAviso = when {
        viajeActual == null || conductorViaje == null ->
            "Aún no tienes viajes con conductor"
        viajeActual.estado == EstadoViaje.EN_CURSO ->
            "Tu viaje está en curso con ${conductorViaje.nombre}"
        viajeActual.estado == EstadoViaje.FINALIZADO ->
            "Tu viaje terminó. ¿Quieres evaluar a ${conductorViaje.nombre}?"
        else ->
            "${conductorViaje.nombre} fue asignado a tu viaje. Patente ${conductorViaje.patente}"
    }

    // Último viaje terminado, para la card Resumen
    val ultimoViaje = viajesViewModel.ultimoViajeFinalizado(viajes, cuenta?.rut.orEmpty())
    Log.d("PERFIL", "rut=${cuenta?.rut} | viajes=${viajes.size} | mios=${viajes.count { it.pasajeroRut == cuenta?.rut }} | conductores=${conductores.size}")
    Log.d("PERFIL", "cuentaRut='${cuenta?.rut}' | ruts en BD=${viajes.map { it.pasajeroRut }.distinct()} | total=${viajes.size}")
    // Reseñas: ¿ya evalué mi último viaje finalizado?
    val resenas by viajesViewModel.resenas.collectAsState()
    val yaEvaluado = ultimoViaje != null && resenas.any { it.viajeId == ultimoViaje.id }

    var mostrarResena by remember { mutableStateOf(false) }
    var estrellas by remember { mutableIntStateOf(0) }
    var comentario by remember { mutableStateOf("") }

    val abrirResena = {
        estrellas = 0
        comentario = ""
        mostrarResena = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Encabezado: volver, foto y nombre completo
        CabeceraRoja(
            alto = 280.dp
        ) {
            Text(
                text = "PERFIL",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, end = 24.dp, bottom = 60.dp)
            ) {
                FotoPerfil(
                    fotoUri = fotoUri,
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = nombreCompleto,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Pasajero",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Encabezado
        TarjetaBlanca(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-44).dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "RESUMEN · ÚLTIMO VIAJE",
                    fontSize = 12.sp,
                    letterSpacing = 4.sp,
                    color = GrisOscuro
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (ultimoViaje == null) {
                    Text(
                        text = "Aún no tienes viajes finalizados",
                        fontSize = 16.sp,
                        color = GrisOscuro
                    )
                } else {
                    val minutos = (ultimoViaje.terminoMillis - ultimoViaje.inicioMillis) / 60000
                    val duracion =
                        if (minutos < 60) "$minutos min" else "${minutos / 60} h ${minutos % 60} min"

                    Text(
                        text = "${nombreLugar(ultimoViaje.origen)} → ${nombreLugar(ultimoViaje.destino)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                    Text(
                        text = "${Fechas.tituloDia(ultimoViaje.fecha)} · ${Fechas.horaTexto(ultimoViaje.hora)}",
                        fontSize = 14.sp,
                        color = GrisOscuro
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        DatoResumen(
                            "%.1f km".format(viajesViewModel.distanciaKm(ultimoViaje)),
                            "Distancia",
                            Modifier.weight(1f)
                        )
                        DatoResumen(duracion, "Duración", Modifier.weight(1f))
                        DatoResumen(ultimoViaje.codigo, "Viaje", Modifier.weight(1f))
                    }
                }
            }
        }

        // Datos del Conductor del vehículo (se llena al escanear el QR)
        TarjetaBlanca(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-30).dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = if (enCurso) "CONDUCTOR · EN VIAJE" else "ÚLTIMO CONDUCTOR",
                    fontSize = 12.sp,
                    letterSpacing = 4.sp,
                    color = GrisOscuro
                )
                Text(
                    text = conductorViaje?.nombre ?: "Aún no tienes conductor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                if (conductorViaje != null) {
                    Text(
                        text = "${conductorViaje.vehiculo} · ${conductorViaje.patente}",
                        fontSize = 14.sp,
                        color = GrisOscuro
                    )
                }
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BotonCirculo(Icons.Default.Call, "Llamar") {
                        val telefono = conductorViaje?.telefono.orEmpty()
                        if (telefono.isBlank()) {
                            Toast.makeText(context, "No hay un conductor con teléfono para llamar", Toast.LENGTH_SHORT).show()
                        } else {
                            val numero = telefono.filter { it.isDigit() || it == '+' }
                            try {
                                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero")))
                            } catch (e: ActivityNotFoundException) {
                                Toast.makeText(context, "Este dispositivo no puede hacer llamadas", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    BotonCirculo(Icons.Default.QrCodeScanner, "Escanear QR del vehículo") {
                        scanner.startScan()
                            .addOnSuccessListener { barcode ->
                                qrViewModel.guardarQR(barcode.rawValue.orEmpty())
                                mostrarDatos = true
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(
                                    context,
                                    "Error: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                    BotonCirculo(Icons.Default.Star, "Evaluar conductor") {
                        when {
                        ultimoViaje == null -> Toast.makeText(
                            context, "Aún no tienes viajes finalizados para evaluar", Toast.LENGTH_SHORT
                        ).show()
                        yaEvaluado -> Toast.makeText(
                            context, "Ya evaluaste tu último viaje", Toast.LENGTH_SHORT
                        ).show()
                        else -> abrirResena()
                    }
                    }
                    BotonCirculo(Icons.Default.Notifications, "Notificación") {
                        mostrarAviso = true
                    }
                }
            }
        }

        // Mis datos: texto y, al pulsar "Editar", se abren los campos
        TarjetaBlanca(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-16).dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MIS DATOS",
                        fontSize = 12.sp,
                        letterSpacing = 4.sp,
                        color = GrisOscuro
                    )
                    TextButton(
                        onClick = {
                            if (editando) registroViewModel.cancelarEdicion()
                            editando = !editando
                        }
                    ) {
                        Text(
                            text = if (editando) "Cancelar" else "Editar",
                            color = RojoBase,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                if (!editando) {
                    // Solo lectura
                    FilaDato("Correo", registroViewModel.mail)
                    FilaDato("Teléfono", registroViewModel.telefono)
                    FilaDato("RUT", registroViewModel.rut)
                    FilaDato("Región", registroViewModel.region)
                    FilaDato("Comuna", registroViewModel.comuna)
                    FilaDato("Dirección", registroViewModel.direccion)
                } else {
                    // Edición
                    CampoPerfil("Nombres", registroViewModel.nombres) {
                        registroViewModel.cambiarNombres(it)
                    }
                    CampoPerfil("Apellidos", registroViewModel.apellidos) {
                        registroViewModel.cambiarApellidos(it)
                    }
                    CampoPerfil("RUT", registroViewModel.rut) {
                        registroViewModel.cambiarRut(it)
                    }
                    CampoPerfil("Correo", registroViewModel.mail) {
                        registroViewModel.cambiarCorreo(it)
                    }
                    CampoPerfil("Teléfono", registroViewModel.telefono) {
                        registroViewModel.cambiarTelefono(it)
                    }
                    SelectorDesplegable(
                        label = "Región",
                        opciones = registroViewModel.regiones,
                        seleccion = registroViewModel.region,
                        onSeleccion = { registroViewModel.cambiarRegion(it) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = coloresCampo()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectorDesplegable(
                        label = "Comuna",
                        opciones = registroViewModel.comunas,
                        seleccion = registroViewModel.comuna,
                        onSeleccion = { registroViewModel.cambiarComuna(it) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = registroViewModel.region.isNotEmpty(),
                        colors = coloresCampo()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CampoPerfil("Dirección", registroViewModel.direccion) {
                        registroViewModel.cambiarDireccion(it)
                    }
                    CampoPerfil("Contraseña", registroViewModel.pass, esPassword = true) {
                        registroViewModel.cambiarPass(it)
                    }
                    OutlinedTextField(
                        value = registroViewModel.codigo,
                        onValueChange = { registroViewModel.agregarCodigo(it) },
                        label = { Text("Código trabajador (opcional)") },
                        singleLine = true,
                        isError = registroViewModel.errorCodigo,
                        supportingText = {
                            if (registroViewModel.errorCodigo) {
                                Text("Código inválido", color = Color.Red)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = coloresCampo()
                    )

                    if (registroViewModel.errorPerfil.isNotEmpty()) {
                        Text(
                            text = registroViewModel.errorPerfil,
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = {
                            // guardarCambios valida y guarda todos los campos en la sesión
                            if (registroViewModel.guardarCambios()) editando = false
                        },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = RojoBase)
                    ) {
                        Text(
                            text = "Guardar",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Cerrar sesión: borra la cuenta activa y vuelve al login
        OutlinedButton(
            onClick = {
                SesionManager.cerrarSesion()
                navController.navigate(Routes.LOGIN) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, RojoBase),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = RojoBase)
        ) {
            Text(text = "Cerrar sesión", fontWeight = FontWeight.ExtraBold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }


    // Ventana con los datos del vehículo escaneado
    if (mostrarDatos) {
        AlertDialog(
            onDismissRequest = { mostrarDatos = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = if (conductorQR == null) "QR no válido" else "Datos del vehículo",
                    fontWeight = FontWeight.ExtraBold
                )
            },
            text = {
                if (conductorQR == null) {
                    Text("Este QR no pertenece a un vehículo de Belmonte.")
                } else {
                    Column {
                        Text("Conductor: ${conductorQR.nombre}")
                        Text("RUT: ${conductorQR.rut}")
                        Text("Vehículo: ${conductorQR.vehiculo}")
                        Text("Patente: ${conductorQR.patente}")

                        // Verificación: ¿el vehículo escaneado es el de mi viaje?
                        Spacer(modifier = Modifier.height(12.dp))
                        when {
                            conductorViaje == null -> Text(
                                text = "No tienes un viaje con conductor asignado para comparar.",
                                color = GrisOscuro,
                                fontSize = 13.sp
                            )
                            conductorQR.id == conductorViaje.id -> Text(
                                text = "✔ Es el conductor de tu viaje",
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.ExtraBold
                            )
                            else -> Text(
                                text = "⚠ Este vehículo NO corresponde a tu viaje",
                                color = Color.Red,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { mostrarDatos = false }) {
                    Text("Cerrar", color = RojoBase)
                }
            },
            dismissButton = {
                // Solo si el QR es válido se ofrece el reclamo
                if (conductorQR != null) {
                    TextButton(onClick = { abrirReclamoSernac(context) }) {
                        Text("Reclamo SERNAC", color = RojoBase)
                    }
                }
            }

        )
    }
    //mensaje notificación
    if (mostrarAviso) {
        AlertDialog(
            onDismissRequest = { mostrarAviso = false },
            containerColor = Color.White,
            title = {
                Text(text = "Notificación", fontWeight = FontWeight.ExtraBold, color = Color.Black)
            },
            text = {
                Text(text = mensajeAviso, color = Color.Black)
            },
            confirmButton = {
                TextButton(onClick = { mostrarAviso = false }) {
                    Text("Cerrar", color = RojoBase)
                }
            },
            dismissButton = {
                if (viajeActual?.estado == EstadoViaje.FINALIZADO && ultimoViaje != null && !yaEvaluado) {
                    TextButton(onClick = {
                        mostrarAviso = false
                        abrirResena()
                    }) {
                        Text("Evaluar", color = RojoBase)
                    }
                }
            }
        )
    }
    //mensaje de evaluación a conductor
    if (mostrarResena && ultimoViaje != null) {
        AlertDialog(
            onDismissRequest = { mostrarResena = false },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Evalúa a ${ultimoViaje.conductorNombre}",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text("Viaje ${ultimoViaje.codigo}", color = GrisOscuro, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        for (n in 1..5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$n estrellas",
                                tint = if (n <= estrellas) Color(0xFFFFB300) else Color.LightGray,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable { estrellas = n }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { if (it.length <= 200) comentario = it },
                        label = { Text("Comentario (opcional)") },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        colors = coloresCampo(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = estrellas > 0,
                    onClick = {
                        viajesViewModel.enviarResena(ultimoViaje, estrellas, comentario) {
                            mostrarResena = false
                            Toast.makeText(context, "¡Gracias por tu evaluación!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        text = "Enviar",
                        color = if (estrellas > 0) RojoBase else GrisOscuro,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarResena = false }) {
                    Text("Cancelar", color = GrisOscuro)
                }
            }
        )
    }
}


// Botón redondo rojo con ícono
@Composable
private fun BotonCirculo(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    descripcion: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(44.dp)
            .background(RojoBase, CircleShape)
    ) {
        Icon(imageVector = icono, contentDescription = descripcion, tint = Color.White)
    }
}

// Un dato del resumen: valor grande y texto abajo
@Composable
private fun DatoResumen(
    valor: String,
    texto: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = valor, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        Text(text = texto, fontSize = 13.sp, color = GrisOscuro)
    }
}

// Fila de solo lectura: título a la izquierda y valor a la derecha
@Composable
private fun FilaDato(titulo: String, valor: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = titulo, fontSize = 14.sp, color = GrisOscuro)
            Text(
                text = if (valor.isBlank()) "-" else valor,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        HorizontalDivider(color = Color.LightGray)
    }
}

// Campo de texto editable con el estilo de la app
@Composable
private fun CampoPerfil(
    titulo: String,
    valor: String,
    esPassword: Boolean = false,
    onCambio: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(titulo) },
        singleLine = true,
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = coloresCampo()
    )
}

@Composable
fun FotoPerfil(
    fotoUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.size(90.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .border(3.dp, Color.White, CircleShape)
                .background(Color.LightGray)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (fotoUri != null) {
                AsyncImage(
                    model = fotoUri,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Ícono de cámara en la esquina
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Cambiar foto",
                tint = Color(0xFFC8102E),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
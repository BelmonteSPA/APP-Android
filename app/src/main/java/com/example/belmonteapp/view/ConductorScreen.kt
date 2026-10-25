package com.example.belmonteapp.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import com.example.belmonteapp.data.SesionManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.belmonteapp.viewModel.PerfilViewModel
import androidx.navigation.NavController
import com.example.belmonteapp.componentes.SelectorDesplegable
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.viewModel.RegistroViewModel
import com.example.belmonteapp.componentes.coloresCampo
import androidx.compose.runtime.collectAsState
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.viewModel.ViajesViewModel
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.util.Fechas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConductorScreen(
    navController: NavController,
    perfilViewModel: PerfilViewModel = viewModel(),
    registroViewModel: RegistroViewModel = viewModel(),
    viajesViewModel: ViajesViewModel = viewModel()


) {
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> if (uri != null) fotoUri = uri }

    var editando by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { registroViewModel.cargarDesdeSesion() }
    //Datos para el encabezado: nombre de la cuenta y patente del vehiculo
    val cuenta = SesionManager.cuentaActual
    val conductores by viajesViewModel.conductores.collectAsState()
    val conductor = conductores.find { it.id == viajesViewModel.conductorId }
    val nombreCompleto = if (cuenta == null) "Conductor" else "${cuenta.nombres} ${cuenta.apellidos}"
    //Resumen con datos reales de Room (solo los viajes de este conductor)
    val viajes by viajesViewModel.viajes.collectAsState()
    val misViajes = viajes.filter { it.conductorId == viajesViewModel.conductorId }
    val viajesHoy = misViajes.count { it.fecha == Fechas.hoy() }
    val finalizados = misViajes.filter { it.estado == EstadoViaje.FINALIZADO }

    //Minutos sumados de todos los viajes finalizados
    val minutos = finalizados.sumOf { (it.terminoMillis - it.inicioMillis) / 60000 }
    val tiempoConducido = if (minutos < 60) "$minutos min" else "${minutos / 60} h ${minutos % 60} min"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)//es el color del fondo
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())//activa la barra de desplazamiento


    ) {
        //Encabezado: foto, nombre completo y patente
        CabeceraRoja(
            alto = 300.dp,
            onVolver = { navController.popBackStack() }
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
                        text = if (conductor == null) "Conductor" else "Conductor · ${conductor.patente}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        //Resumen del conductor (se monta sobre el encabezado)
        TarjetaBlanca(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-44).dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "RESUMEN",
                    fontSize = 12.sp,
                    letterSpacing = 4.sp,
                    color = GrisOscuro
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    DatoResumen(
                        valor = viajesHoy.toString(),
                        texto = "Viajes hoy",
                        modifier = Modifier.weight(1f)
                    )
                    DatoResumen(
                        valor = finalizados.size.toString(),
                        texto = "Finalizados",
                        modifier = Modifier.weight(1f)
                    )
                    DatoResumen(
                        valor = tiempoConducido,
                        texto = "Conducido",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        //Vehiculo del conductor (desde la tabla conductor de Room)
        TarjetaBlanca(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset(y = (-30).dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Icono del auto en un cuadro rojo claro
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(RojoBase.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = "Vehículo",
                        tint = RojoBase
                    )
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "MI VEHÍCULO",
                        fontSize = 12.sp,
                        letterSpacing = 4.sp,
                        color = GrisOscuro
                    )
                    Text(
                        text = conductor?.vehiculo ?: "Sin vehículo asignado",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (conductor != null) {
                        Text(
                            text = "Patente ${conductor.patente}",
                            fontSize = 14.sp,
                            color = GrisOscuro
                        )
                    }
                }
            }
        }
        //Mis datos: se muestran como texto y al pulsar "Editar" se abren los campos
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
                    //Solo lectura
                    FilaDato("Correo", registroViewModel.mail)
                    FilaDato("Teléfono", registroViewModel.telefono)
                    FilaDato("RUT", registroViewModel.rut)
                    FilaDato("Región", registroViewModel.region)
                    FilaDato("Comuna", registroViewModel.comuna)
                    FilaDato("Dirección", registroViewModel.direccion)
                } else {
                    //Edicion
                    CampoPerfil("Nombres", registroViewModel.nombres) { registroViewModel.cambiarNombres(it) }
                    CampoPerfil("Apellidos", registroViewModel.apellidos) { registroViewModel.cambiarApellidos(it) }
                    CampoPerfil("RUT", registroViewModel.rut) { registroViewModel.cambiarRut(it) }
                    CampoPerfil("Correo", registroViewModel.mail) { registroViewModel.cambiarCorreo(it) }
                    CampoPerfil("Teléfono", registroViewModel.telefono) { registroViewModel.cambiarTelefono(it) }
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
                    CampoPerfil("Dirección", registroViewModel.direccion) { registroViewModel.cambiarDireccion(it) }
                    CampoPerfil("Contraseña", registroViewModel.pass, esPassword = true) { registroViewModel.cambiarPass(it) }

                    if (registroViewModel.errorPerfil.isNotEmpty()) {
                        Text(
                            text = registroViewModel.errorPerfil,
                            color = Color.Red,
                            fontSize = 13.sp
                        )
                    }
                    Button(
                        onClick = {
                            //guardarCambios valida y guarda todos los campos en la sesion
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
    }
}
//Un dato del resumen: valor grande y texto abajo
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
        Text(
            text = valor,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = texto,
            fontSize = 13.sp,
            color = GrisOscuro
        )
    }
}

//Fila de solo lectura: titulo a la izquierda y valor a la derecha
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

//Campo de texto editable con el estilo de la app
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

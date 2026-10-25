package com.example.belmonteapp.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import com.example.belmonteapp.data.SesionManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.belmonteapp.viewModel.PerfilViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.belmonteapp.componentes.SelectorDesplegable
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.viewModel.RegistroViewModel
import com.example.belmonteapp.componentes.coloresCampo
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.collectAsState
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.viewModel.ViajesViewModel
import com.example.belmonteapp.componentes.TarjetaBlanca
import com.example.belmonteapp.modelo.EstadoViaje
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.util.Fechas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdmScreen(
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
    //Nombre de la cuenta con sesion iniciada
    val cuenta = SesionManager.cuentaActual
    val nombreCompleto = if (cuenta == null) "Administrador" else "${cuenta.nombres} ${cuenta.apellidos}"
    //Resumen de todos los viajes (datos reales de Room)
    val viajes by viajesViewModel.viajes.collectAsState()
    val conductores by viajesViewModel.conductores.collectAsState()
    val viajesHoy = viajes.count { it.fecha == Fechas.hoy() }
    val pendientes = viajes.count { it.estado == EstadoViaje.PENDIENTE }
    val enCurso = viajes.count { it.estado == EstadoViaje.EN_CURSO }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)//es el color del fondo
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())//activa la barra de desplazamiento


    ) {
        //Encabezado: foto, nombre completo y rol
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
                    }
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = nombreCompleto,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Administrador · Belmonte SpA",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
        //Resumen de viajes (se monta sobre el encabezado)
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
                    DatoAdmin(viajesHoy.toString(), "Viajes hoy", Modifier.weight(1f))
                    DatoAdmin(pendientes.toString(), "Pendientes", Modifier.weight(1f), resaltado = true)
                    DatoAdmin(enCurso.toString(), "En curso", Modifier.weight(1f))
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 24.dp)
                .offset(y = (-35).dp) // sube la segunda tarjeta para juntarla a la primera
                .shadow(16.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Conductor",
                    color = Color.Black,
                    fontSize = 18.sp,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                Text(
                    text = "Nombre Conductor",
                    color = Color.Black,
                    fontSize = 20.sp,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Separa los textos a los extremos
                ) {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.PERFIL)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(RojoBase, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Llamar",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.PERFIL)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(RojoBase, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Mensaje",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.PERFIL)
                        },
                        modifier = Modifier
                            .size(44.dp)
                            .background(RojoBase, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Natificación",
                            tint = Color.White
                        )
                    }
                }

            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(1210.dp)//altura de la card
                .padding(horizontal = 24.dp)
                .offset(y = (-25).dp) // sube la segunda tarjeta para juntarla a la primera
                .shadow(16.dp, RoundedCornerShape(28.dp)),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Button(
                    onClick = {
                        if (editando) registroViewModel.cancelarEdicion()
                        editando = !editando
                    },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.End) // Alinea el botón a la derecha
                        .wrapContentWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBase
                    )
                ){
                    Text(
                        text = if (editando) "Cancelar" else "Editar",
                        color = Color.White
                    )
                }
                Text(
                    text = "Nombres",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.nombres,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarNombres(it)},
                    label = {
                        Text("Ej: Juan Antonio")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()

                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Apellidos",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.apellidos,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarApellidos(it)},
                    label = {
                        Text("Ej: Perez Gonzalez")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Rut",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.rut,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarRut(it)},
                    label = {
                        Text("Ej: 00.000.000-0")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Región",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                SelectorDesplegable(
                    label = "",
                    opciones = registroViewModel.regiones,
                    seleccion = registroViewModel.region,
                    onSeleccion = { registroViewModel.cambiarRegion(it) },
                    enabled = editando,
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Comuna",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                SelectorDesplegable(
                    label = "",
                    opciones = registroViewModel.comunas,
                    seleccion = registroViewModel.comuna,
                    onSeleccion = { registroViewModel.cambiarComuna(it) },
                    enabled = editando && registroViewModel.region.isNotEmpty(),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Dirección",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.direccion,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarDireccion(it)},
                    label = {
                        Text("Ej: Calle 1")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Correo",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.mail,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarCorreo(it)},
                    label = {
                        Text("Ej: tuNombre@mail.com")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Teléfono",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.telefono,
                    onValueChange = {
                        registroViewModel.cambiarTelefono(it)
                    },
                    label = {
                        Text("Ej: +56911110000")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Contraseña",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.pass,
                    enabled = editando,
                    onValueChange = {registroViewModel.cambiarPass(it)},
                    label = {
                        Text("Contraseña")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    colors = coloresCampo()
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Ingresa Código Trabajador",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.codigo,
                    onValueChange = {
                        registroViewModel.agregarCodigo(it)
                    },
                    label = {
                        Text("Código opcional")
                    },
                    isError = registroViewModel.errorCodigo,
                    supportingText = {
                        if (registroViewModel.errorCodigo) Text("Código inválido", color = Color.Red)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (registroViewModel.errorPerfil.isNotEmpty()) {
                    Text(registroViewModel.errorPerfil, color = Color.Red)
                }
                Button(
                    onClick = {
                        SesionManager.cuentaActual?.let { actual ->
                            SesionManager.actualizarCuenta(
                                actual.copy(
                                    nombres = registroViewModel.nombres,
                                    apellidos = registroViewModel.apellidos,
                                    rut = registroViewModel.rut
                                )
                            )
                        }
                        editando = false

                    },
                    enabled = editando,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBase
                    )
                ){
                    Text(
                        text = "Guardar",
                        color = Color.White
                    )
                }


            }
        }
    }
}


//Un dato del resumen: numero grande y texto abajo
@Composable
private fun DatoAdmin(
    valor: String,
    texto: String,
    modifier: Modifier = Modifier,
    resaltado: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valor,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (resaltado) RojoBase else Color.Black
        )
        Text(
            text = texto,
            fontSize = 13.sp,
            color = GrisOscuro
        )
    }
}

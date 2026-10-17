package com.example.belmonteapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.R
import com.example.belmonteapp.componentes.SelectorDesplegable
import com.example.belmonteapp.navigation.Routes
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.viewModel.RegistroViewModel

@Composable

fun RegistroScreen(
    navController: NavController,
    registroViewModel: RegistroViewModel = viewModel()
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())//activa la barra de desplazamiento

    ) {
        //Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = RojoBase,
                    shape = RoundedCornerShape(
                        bottomStart = 56.dp, bottomEnd = 56.dp
                    )
                )

        ) {
            IconButton(
                onClick = {
                    navController.navigate(Routes.HOME)
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 8.dp)
                    .size(44.dp)
                    .background(Color.White.copy(alpha = 0.18f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)//agrega expacio extra
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo de la app",
                    modifier = Modifier.size(104.dp),
                    contentScale = ContentScale.Inside,
                )
                Spacer(modifier = Modifier.height(15.dp))

            }

        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(1200.dp)
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                .offset(y = (-44).dp) // la sube para que se monte sobre el rojo
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
                    text = "Crear Cuenta",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Nombres",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.nombres,
                    onValueChange = {
                        registroViewModel.agregarNombre(it)
                    },
                    label = {
                        Text("Ej: Juan Antonio")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
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
                    onValueChange = {
                        registroViewModel.agregarApellido(it)
                    },
                    label = {
                        Text("Ej: Perez Gonzalez")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
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
                    onValueChange = {
                        registroViewModel.agregarRut(it)
                    },
                    label = {
                        Text("Ej: 00.000.000-0")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
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
                    onSeleccion = { registroViewModel.cambiarRegion(it) }
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
                    enabled = registroViewModel.region.isNotEmpty()
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
                    value = registroViewModel.rut,
                    onValueChange = {
                        registroViewModel.agregarRut(it)
                    },
                    label = {
                        Text("Ej: Calle 1")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
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
                    onValueChange = {
                        registroViewModel.agregarMail(it)
                    },
                    label = {
                        Text("Ej: tuNombre@mail.com")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
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
                    onValueChange = {
                        registroViewModel.agregarPass(it)
                    },
                    label = {
                        Text("Contraseña")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Confirmar Contraseña",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                )
                OutlinedTextField(
                    value = registroViewModel.pass,
                    onValueChange = {
                        registroViewModel.agregarPass(it)
                    },
                    label = {
                        Text("Confirmar Contraseña")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        navController.navigate(Routes.CONDUCTOR)

                    },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoBase
                    )
                ){
                    Text(
                        text = "Registrar",
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Ya tienes cuenta?",
                    modifier = Modifier.padding(top = 10.dp)
                )
                TextButton(
                    onClick = {navController.navigate(Routes.LOGIN) }
                ) {
                    Text(
                        text = "Inicia Sesión aquí",
                        color = RojoBase
                    )
                }

            }
        }
    }

}
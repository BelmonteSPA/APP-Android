package com.example.belmonteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Class
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SystemSecurityUpdateGood
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.belmonteapp.ui.theme.RojoBase
import com.example.belmonteapp.viewModel.ConfiViewModel

@Composable
fun ConfiScreen(
    navController: NavController,
    confiViewModel: ConfiViewModel = viewModel()
){
    Column(
        modifier = Modifier
            .background(Color.White)//es el color del fondo
            .fillMaxSize()
            .navigationBarsPadding()
            .background(RojoBase)
            .verticalScroll(rememberScrollState())//activa la barra de desplazamiento


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "Configuración",
                    fontSize = 25.sp,//tamaño de la fuente
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Llamar",
                        tint = Color.White
                    )
                    Text(text = "Notificaciones")
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Cuenta",
                    modifier = Modifier.height(50.dp),
                    fontSize = 25.sp,//tamaño de la fuente
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.SystemSecurityUpdateGood,
                        contentDescription = "Llamar",
                        tint = Color.White
                    )
                    Text(text = "Seguridad y Permisos")
                }
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Class,
                        contentDescription = "Llamar",
                        tint = Color.White
                    )
                    Text(text = "Terminos y Condiciones")
                }
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Llamar",
                        tint = Color.White
                    )
                    Text(
                        text = "Cerrar Sesión",
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}
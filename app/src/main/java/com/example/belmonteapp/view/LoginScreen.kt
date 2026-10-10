package com.example.belmonteapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.belmonteapp.viewmodel.LoginViewModel


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Iniciar Sesión",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        OutlinedTextField(
            value = loginViewModel.mail,
            onValueChange = {
                loginViewModel.cambiarMail(it)
            },
            label = {
                Text("Correo Electrónico")
            },
            modifier = Modifier.fillMaxSize()
        )
        OutlinedTextField(
            value = loginViewModel,
            onValueChange = {},
            label = {
                Text("Contraseña")
            },
            modifier = Modifier.fillMaxSize()
        )
        Button(
            onClick = {},
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth()
        ) {
            Text(text = "Ingresar")
        }
    }
}
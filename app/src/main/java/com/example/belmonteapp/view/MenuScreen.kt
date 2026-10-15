package com.example.belmonteapp.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun MenuScreen(){
    var selector by remember { mutableIntStateOf(2) }

    val items = listOf(
        Triple("Mis viajes", Icons.Outlined.LocationOn, Icons.Filled.LocationOn),
        Triple("Nuevo viaje", Icons.Outlined.Add, Icons.Filled.Add),
        Triple("Perfil", Icons.Outlined.Person, Icons.Filled.Person),
        Triple("Configuración", Icons.Outlined.Settings, Icons.Filled.Settings),
    )

}
package com.example.belmonteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.belmonteapp.ui.theme.RojoBase
import android.annotation.SuppressLint
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.miapp.NuevoViajeScreen
import com.example.belmonteapp.modelo.ItemMenu
import com.example.belmonteapp.modelo.Tipo
import com.example.belmonteapp.viewModel.MenuViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuScreen(
    navController: NavController,
    menuViewModel: MenuViewModel = viewModel()
) {

    // 0 = Mis viajes, 1 = Nuevo viaje, 2 = Perfil, 3 = Configuración
    var selector by remember { mutableIntStateOf(2) }

    val items = listOf(
        ItemMenu("Mis viajes", Icons.Outlined.LocationOn, Icons.Filled.LocationOn),
        ItemMenu("Nuevo viaje", Icons.Outlined.DirectionsCar, Icons.Filled.DirectionsCar),
        ItemMenu("Perfil", Icons.Outlined.Person, Icons.Filled.Person),
        ItemMenu("Configuración", Icons.Outlined.Settings, Icons.Filled.Settings),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF8EDEB) // fondo rosado suave de la barra
            ) {
                items.forEachIndexed { index, item ->
                    val seleccionado = selector == index

                    NavigationBarItem(
                        selected = seleccionado,
                        onClick = { selector = index },
                        icon = {
                            Icon(
                                imageVector = if (seleccionado) item.iconoSeleccionado else item.iconoNormal,
                                contentDescription = item.titulo
                            )
                        },
                        label = {
                            Text(
                                text = item.titulo,
                                fontSize = 11.sp,
                                // el texto seleccionado va en negrita
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.DarkGray,
                            unselectedTextColor = Color.DarkGray,
                            indicatorColor = RojoBase.copy(alpha = 0.2f) // la "píldora" detrás del icono
                        )
                    )
                }
            }
        }
    ) { _ ->
        // Aquí se muestra la pantalla según la opción elegida
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (selector) {
                0 -> PantallaTemporal("Mis viajes")
                1 -> NuevoViajeScreen(navController)
                2 -> {

                    when (menuViewModel.rutaPerfil) {
                        Tipo.CONDUCTOR -> ConductorScreen(navController)
                        Tipo.ADMINISTRADOR -> AdmScreen(navController)
                        else -> PerfilScreen(navController)
                    }
                }
                3 -> PantallaTemporal("Configuración")
            }
        }
    }
}

// Pantalla de relleno mientras creas "Mis viajes" y "Configuración"
@Composable
private fun PantallaTemporal(titulo: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = titulo)
    }
}
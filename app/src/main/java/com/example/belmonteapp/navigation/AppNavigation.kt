package com.example.belmonteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.belmonteapp.view.AdmScreen
import com.example.belmonteapp.view.HomeScreen
import com.example.belmonteapp.view.LoginScreen
import com.example.belmonteapp.view.PerfilScreen
import com.example.belmonteapp.view.RegistroScreen
import com.example.belmonteapp.view.NuevoViajeScreen
import com.example.belmonteapp.view.ConductorScreen
import com.example.belmonteapp.view.MenuScreen

@Composable


fun AppNavigation(){
    val navController = rememberNavController()

    //aqui se agregan las rutas de las pantallas
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ){
        composable(Routes.LOGIN){LoginScreen(navController)}
        composable(Routes.HOME){ HomeScreen(navController) }
        composable (Routes.REGISTRO){ RegistroScreen(navController) }
        composable(Routes.NUEVO_VIAJE) { NuevoViajeScreen(navController) }
        composable (Routes.CONDUCTOR){ ConductorScreen(navController) }
        composable (Routes.ADMIN){ AdmScreen(navController) }
        composable (Routes.MENU){ MenuScreen(navController) }
    }
}
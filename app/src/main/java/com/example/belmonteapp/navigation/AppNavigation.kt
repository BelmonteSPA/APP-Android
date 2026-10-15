package com.example.belmonteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.belmonteapp.view.HomeScreen
import com.example.belmonteapp.view.LoginScreen
import com.example.belmonteapp.view.PerfilScreen
import com.example.belmonteapp.view.RegistroScreen
import com.example.belmonteapp.view.NuevoViajeScreen

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
        composable (Routes.PERFIL){ PerfilScreen(navController) }
        composable(Routes.NUEVO_VIAJE) { NuevoViajeScreen(navController) }
    }
}
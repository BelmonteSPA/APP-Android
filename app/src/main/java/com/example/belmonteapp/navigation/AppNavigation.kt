package com.example.belmonteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ejemplo.miapp.NuevoViajeScreen
import com.example.belmonteapp.view.AdmScreen
import com.example.belmonteapp.view.HomeScreen
import com.example.belmonteapp.view.LoginScreen
import com.example.belmonteapp.view.RegistroScreen
import com.example.belmonteapp.view.ConductorScreen
import com.example.belmonteapp.view.ConfiScreen
import com.example.belmonteapp.view.MenuScreen
import com.example.belmonteapp.view.ViajeScreen
import com.example.belmonteapp.view.ViajesConductorScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.belmonteapp.view.AsignarConductorScreen
import com.example.belmonteapp.view.DetalleViajeScreen
import com.example.belmonteapp.view.MisViajesScreen
import com.example.belmonteapp.view.ModificarAsignacionScreen
import com.example.belmonteapp.view.PendientesScreen
import com.example.belmonteapp.view.ViajeEnCursoScreen
import com.example.belmonteapp.view.ViajeFinalizadoScreen
import com.example.belmonteapp.view.ViajesFechaScreen

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
        composable (Routes.VIAJE){ ViajeScreen(navController) }
        composable (Routes.MIS_VIAJES){ MisViajesScreen(navController) }
        composable(Routes.VIAJES_DIA) { ViajesConductorScreen(navController, soloHoy = true) }
        composable(Routes.VIAJES_ASIGNADOS) { ViajesConductorScreen(navController, soloHoy = false) }
        composable(
            route = Routes.DETALLE_VIAJE,
            arguments = listOf(navArgument("viajeId") { type = NavType.IntType })
        ) { entrada ->
            DetalleViajeScreen(navController, entrada.arguments?.getInt("viajeId") ?: 0)
        }
        composable(
            route = Routes.VIAJE_EN_CURSO,
            arguments = listOf(navArgument("viajeId") { type = NavType.IntType })
        ) { entrada ->
            ViajeEnCursoScreen(navController, entrada.arguments?.getInt("viajeId") ?: 0)
        }
        composable(
            route = Routes.VIAJE_FINALIZADO,
            arguments = listOf(navArgument("viajeId") { type = NavType.IntType })
        ) { entrada ->
            ViajeFinalizadoScreen(navController, entrada.arguments?.getInt("viajeId") ?: 0)
        }
        composable(Routes.VIAJES_FECHA) { ViajesFechaScreen(navController) }
        composable(
            route = Routes.ASIGNAR_CONDUCTOR,
            arguments = listOf(navArgument("viajeId") { type = NavType.IntType })
        ) { entrada ->
            AsignarConductorScreen(navController, entrada.arguments?.getInt("viajeId") ?: 0)
        }
        composable(
            route = Routes.MODIFICAR_ASIGNACION,
            arguments = listOf(navArgument("viajeId") { type = NavType.IntType })
        ) { entrada ->
            ModificarAsignacionScreen(navController, entrada.arguments?.getInt("viajeId") ?: 0)
        }
        composable(Routes.PENDIENTES) { PendientesScreen(navController) }
    }

}
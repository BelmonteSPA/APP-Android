package com.example.belmonteapp.componentes

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.belmonteapp.ui.theme.GrisOscuro
import com.example.belmonteapp.ui.theme.RojoBase

//cada opción de la barra, texto e icono
data class OpcionBarra (
    val texto : String,
    val icono : ImageVector
)

//barra de abajo con pestañas
@Composable
fun BarraInferior(
    opciones : List<OpcionBarra>,
    seleccionada : Int,
    onSeleccion : (Int) -> Unit
){
    NavigationBar(containerColor = Color.White) {
        opciones.forEachIndexed { indice, opcion ->
            NavigationBarItem(
                selected = indice == seleccionada,
                onClick = { onSeleccion(indice) },
                icon = {
                    Icon(
                        imageVector = opcion.icono,
                        contentDescription = opcion.texto
                    )
                },
                label = { Text(opcion.texto) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = RojoBase,
                    indicatorColor = RojoBase,
                    unselectedIconColor = GrisOscuro,
                    unselectedTextColor = GrisOscuro
                )
            )
        }
    }
}
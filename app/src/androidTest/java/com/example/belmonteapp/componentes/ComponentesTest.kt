package com.example.belmonteapp.componentes

import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.belmonteapp.modelo.EstadoViaje
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

//Pruebas de UI (se ejecutan en el emulador) de los componentes reutilizables
class ComponentesTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun etiquetaEstado_muestra_el_estado_en_mayusculas() {
        composeRule.setContent {
            EtiquetaEstado(estado = EstadoViaje.EN_CURSO)
        }

        composeRule.onNodeWithText("EN CURSO").assertIsDisplayed()
    }

    @Test
    fun barraInferior_muestra_las_opciones_y_avisa_cual_se_toco() {
        var tocada = -1
        val opciones = listOf(
            OpcionBarra("Pendientes", Icons.Default.Schedule),
            OpcionBarra("Viajes", Icons.Default.DateRange),
            OpcionBarra("Perfil", Icons.Default.Person)
        )

        composeRule.setContent {
            BarraInferior(
                opciones = opciones,
                seleccionada = 0,
                onSeleccion = { tocada = it }
            )
        }

        composeRule.onNodeWithText("Pendientes").assertIsDisplayed()
        composeRule.onNodeWithText("Viajes").assertIsDisplayed()
        composeRule.onNodeWithText("Perfil").performClick()

        assertEquals(2, tocada)
    }
}

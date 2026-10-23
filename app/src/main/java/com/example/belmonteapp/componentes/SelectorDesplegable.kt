package com.example.belmonteapp.componentes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorDesplegable (
    label: String,
    opciones: List<String>,
    seleccion: String,
    onSeleccion: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: TextFieldColors = coloresCampo()
){
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(//Le dice al contenedor si el menú está abierto.
        expanded = expandido,
        onExpandedChange = { if (enabled) expandido = it },
        modifier = modifier
    )
    {
        OutlinedTextField(
            value = seleccion,//valor de la lista desplegable
            onValueChange = {},
            readOnly = true,
            enabled = enabled, //Pasa el estado habilitado/deshabilitado al campo.
            label = { Text(label) }, //Muestra el título (“Región”, “Comuna”)
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) //icono de la lista desplegable
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion -> //Recorre la lista y crea un elemento por cada opción.s
                DropdownMenuItem(//opciones de la lista desplegable
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        expandido = false
                    }
                )
            }
        }
    }

}
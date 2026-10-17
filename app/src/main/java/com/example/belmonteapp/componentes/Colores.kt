package com.example.belmonteapp.componentes

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.belmonteapp.ui.theme.Gris
import com.example.belmonteapp.ui.theme.RojoBase

@Composable
fun coloresCampo() = OutlinedTextFieldDefaults.colors(
    // Bloqueado
    disabledTextColor = Gris, // texto escrito
    disabledBorderColor = Gris,// borde
    disabledLabelColor = Gris,// label (el "Ej: ...")
    disabledContainerColor = Color.White,// fondo

    // Editable, sin seleccionar
    unfocusedTextColor = Color.Black,
    unfocusedBorderColor = Color.Black,
    unfocusedLabelColor = Color.Black,
    unfocusedContainerColor = Color.White,

    // Editable y seleccionado (cursor ahí)
    focusedTextColor = Color.Black,
    focusedBorderColor = RojoBase,
    focusedLabelColor = RojoBase,
    focusedContainerColor = Color.White,
    cursorColor = RojoBase
)
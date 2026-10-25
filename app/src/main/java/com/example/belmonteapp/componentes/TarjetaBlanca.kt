package com.example.belmonteapp.componentes

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

//Tarjeta blanca con sombra y esquinas redondeadas (la misma de las otras pantallas)
@Composable
fun TarjetaBlanca(
    modifier: Modifier = Modifier,
    radio: Dp = 28.dp,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.shadow(16.dp, RoundedCornerShape(radio)),
        shape = RoundedCornerShape(radio),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = contenido
    )
}
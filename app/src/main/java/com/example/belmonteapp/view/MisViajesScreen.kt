package com.example.belmonteapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.Text
import com.example.belmonteapp.componentes.CabeceraRoja
import com.example.belmonteapp.viewModel.NuevoViajeViewModel
import com.example.belmonteapp.viewModel.ViajesViewModel

@Composable
fun MisViajesScreen(
    navController: NavController,
    nuevoViajeViewModel: NuevoViajeViewModel = viewModel(),
    viajesViewModel: ViajesViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        CabeceraRoja(
            alto = 200.dp
        ) {

            Text(
                text = "Mis Viajes",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(start = 20.dp, top = 20.dp)

            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y= (-25).dp)
        ) {

        }
    }
}
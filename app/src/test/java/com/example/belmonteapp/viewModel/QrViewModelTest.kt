package com.example.belmonteapp.viewModel

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

//Prueba del ViewModel que lee el QR pegado en los autos ("BELMONTE:<patente>")
class QrViewModelTest : StringSpec({

    "patente() devuelve la patente cuando el QR es de Belmonte" {
        val viewModel = QrViewModel()

        viewModel.guardarQR("BELMONTE:KDRT-42")

        viewModel.patente() shouldBe "KDRT-42"
    }

    "patente() devuelve null cuando el QR no es de Belmonte" {
        val viewModel = QrViewModel()

        viewModel.guardarQR("https://www.google.com")

        viewModel.patente() shouldBe null
    }

    "guardarQR() actualiza el contenido que observa la pantalla" {
        val viewModel = QrViewModel()

        viewModel.guardarQR("BELMONTE:PLZK-07")

        viewModel.contenidoQR.value shouldBe "BELMONTE:PLZK-07"
    }
})

package com.example.belmonteapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    var mail by mutableStateOf("")

    fun cambiarMail(nuevoMail: String){
        mail = nuevoMail
    }
}
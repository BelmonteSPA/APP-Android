package com.example.belmonteapp.data

import com.example.belmonteapp.modelo.Cuenta
import com.example.belmonteapp.modelo.Tipo
import com.example.belmonteapp.modelo.Usuario
import com.example.belmonteapp.modelo.Viajes

class DataP{

    object UsuariosPrueba {
        val cuentas = listOf(
            Cuenta(
                0, "David Patricio", "Hernandez Valenzuela", "12.345.678-9",
                Usuario(0, "pasajero@belmonte.cl", "123"), Tipo.PASAJERO
            ),
            Cuenta(1, "Juan", "Pérez", "11.111.111-1",
                Usuario(1, "conductor@belmonte.cl", "123"), Tipo.CONDUCTOR),
            Cuenta(2, "Ana", "Soto", "22.222.222-2",
                Usuario(2, "admin@belmonte.cl", "123"), Tipo.ADMINISTRADOR)
        )
    }
    object CodigosTrabajador {
        val validos = listOf("COND-001", "COND-002", "COND-003")


        fun esValido(codigo: String) = codigo.trim().uppercase() in validos
    }
    /*object viajes{
        val viajes = listOf(
            Viajes(0, )
    }*/
}

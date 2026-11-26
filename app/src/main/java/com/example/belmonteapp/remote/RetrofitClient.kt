package com.example.belmonteapp.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Crea Retrofit una sola vez (object = singleton) y lo comparte toda la app
object RetrofitClient {

    //10.0.2.2 es el "localhost" del computador visto desde el emulador.
    //8090 es el puerto del api-gateway: todas las peticiones pasan por ahi.
    private const val BASE_URL = "http://10.0.2.2:8090/api/v1/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //JSON <-> clases Kotlin
            .build()
            .create(ApiService::class.java)
    }
}
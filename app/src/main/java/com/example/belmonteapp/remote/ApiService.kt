package com.example.belmonteapp.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//Endpoints del backend que usa la app (todos pasan por el api-gateway).
//token = "Bearer <jwt>" que entrega el login; el gateway lo valida y revisa el rol.
interface ApiService {

    //ms-auth: unico endpoint sin token
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequest): LoginResponse

    //ms-viajes: administrador
    @GET("viajes")
    suspend fun obtenerViajes(@Header("Authorization") token: String): List<ViajeDto>

    @PUT("viajes/{id}/asignar")
    suspend fun asignarConductor(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body datos: AsignarRequest
    ): ViajeDto

    //ms-viajes: conductor
    @GET("viajes/conductor/{id}")
    suspend fun viajesDelConductor(
        @Header("Authorization") token: String,
        @Path("id") idConductor: Int
    ): List<ViajeDto>

    @PATCH("viajes/{id}/iniciar")
    suspend fun iniciarViaje(@Header("Authorization") token: String, @Path("id") id: Int): ViajeDto

    @PATCH("viajes/{id}/finalizar")
    suspend fun finalizarViaje(@Header("Authorization") token: String, @Path("id") id: Int): ViajeDto

    //ms-viajes: pasajero
    @GET("viajes/pasajero/{id}")
    suspend fun viajesDelPasajero(
        @Header("Authorization") token: String,
        @Path("id") idPasajero: Int
    ): List<ViajeDto>

    @POST("viajes")
    suspend fun crearViaje(@Header("Authorization") token: String, @Body viaje: NuevoViajeRequest): ViajeDto

    //ms-conductores: administrador
    @GET("conductores")
    suspend fun obtenerConductores(@Header("Authorization") token: String): List<ConductorDto>
}
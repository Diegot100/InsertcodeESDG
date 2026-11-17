package com.example.insertcodeesalasdg.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface EventosApi {

    @GET("rest/v1/eventos")
    suspend fun obtenerEventos(
        @Query("select") seleccion: String = "*",
        @Query("order") orden: String = "creado_en.desc"
    ): List<EventoDto>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/eventos")
    suspend fun actualizarEvento(
        @Query("id") filtroId: String,
        @Body cuerpo: ActualizarEventoRequest
    ): List<EventoDto>

    @Headers("Prefer: return=minimal")
    @POST("rest/v1/acciones")
    suspend fun registrarAccion(@Body cuerpo: AccionRequest)

    @GET("rest/v1/acciones")
    suspend fun obtenerAcciones(
        @Query("select") seleccion: String = "*",
        @Query("order") orden: String = "creado_en.desc",
        @Query("limit") limite: Int = 20
    ): List<AccionDto>
}
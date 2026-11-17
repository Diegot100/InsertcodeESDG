package com.example.insertcodeesalasdg.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Headers
import retrofit2.http.PATCH

interface SupabaseApi {

    @POST("auth/v1/token")
    suspend fun login(
        @Query("grant_type") grantType: String,
        @Body cuerpo: LoginRequest
    ): SesionResponse

    @GET("rest/v1/perfiles")
    suspend fun obtenerPerfil(
        @Query("id") filtroId: String,
        @Query("select") seleccion: String = "id,nombre,rol"
    ): List<PerfilDto>

    @POST("auth/v1/token")
    suspend fun loginConIdToken(
        @Query("grant_type") grantType: String,
        @Body cuerpo: IdTokenRequest
    ): SesionResponse

    @GET("rest/v1/camaras")
    suspend fun obtenerCamaras(
        @Query("select") seleccion: String = "*",
        @Query("order") orden: String = "id.asc"
    ): List<CamaraDto>

    @Headers("Prefer: return=representation")
    @PATCH("rest/v1/camaras")
    suspend fun actualizarCamara(
        @Query("id") filtroId: String,
        @Body cuerpo: ActualizarCamaraRequest
    ): List<CamaraDto>
}
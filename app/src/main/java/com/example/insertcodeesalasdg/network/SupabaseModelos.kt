package com.example.insertcodeesalasdg.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class UsuarioAuth(
    val id: String? = null,
    val email: String? = null
)

data class SesionResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    val user: UsuarioAuth? = null
)

data class PerfilDto(
    val id: String? = null,
    val nombre: String? = null,
    val rol: String? = null
)

data class IdTokenRequest(
    val provider: String,
    @SerializedName("id_token") val idToken: String,
    val nonce: String
)
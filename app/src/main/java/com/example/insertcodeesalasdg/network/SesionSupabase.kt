package com.example.insertcodeesalasdg.network

object SesionSupabase {
    @Volatile var accessToken: String? = null
    @Volatile var usuarioId: String? = null
    @Volatile var email: String? = null

    fun limpiar() {
        accessToken = null
        usuarioId = null
        email = null
    }
}
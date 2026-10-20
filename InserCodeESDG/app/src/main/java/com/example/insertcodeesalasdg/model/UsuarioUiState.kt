package com.example.insertcodeesalasdg.model

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val aceptaTerminos: Boolean = false,
    val rol: String = "Guardia",
    val estaGuardando: Boolean = false,
    val esExitoso: Boolean = false
)
package com.example.insertcodeesalasdg.model

// Guía 11: mensajes de error de validación (null = sin error)
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null,
    val terminos: String? = null
)

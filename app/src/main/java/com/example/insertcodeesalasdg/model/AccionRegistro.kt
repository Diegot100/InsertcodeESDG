package com.example.insertcodeesalasdg.model

data class AccionRegistro(
    val id: Long,
    val eventoId: Long,
    val usuario: String,
    val accion: String,
    val fecha: String
)
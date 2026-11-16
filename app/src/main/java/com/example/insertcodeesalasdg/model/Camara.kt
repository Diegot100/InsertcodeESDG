package com.example.insertcodeesalasdg.model

data class Camara(
    val id: Int,
    val nombre: String,
    val zona: String,
    val estado: EstadoCamara,
    val grabando: Boolean
)
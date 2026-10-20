package com.example.insertcodeesalasdg.model

data class EventoAlerta(
    val id: String,
    val camaraId: String,
    val ubicacionReferencial: String,
    val prioridad: Prioridad,
    val estadoAtencion: EstadoAtencion,
    val descripcion: String
)
package com.example.insertcodeesalasdg.network

import com.google.gson.annotations.SerializedName

data class EventoDto(
    val id: Long = 0,
    @SerializedName("camara_id") val camaraId: Int? = null,
    @SerializedName("ubicacion_referencial") val ubicacionReferencial: String = "",
    val prioridad: String = "",
    @SerializedName("estado_atencion") val estadoAtencion: String = "",
    val descripcion: String = "",
    @SerializedName("creado_en") val creadoEn: String? = null
)

data class ActualizarEventoRequest(
    @SerializedName("estado_atencion") val estadoAtencion: String
)

data class AccionRequest(
    @SerializedName("evento_id") val eventoId: Long,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("usuario_email") val usuarioEmail: String?,
    val accion: String
)

data class AccionDto(
    val id: Long = 0,
    @SerializedName("evento_id") val eventoId: Long = 0,
    @SerializedName("usuario_email") val usuarioEmail: String? = null,
    val accion: String = "",
    @SerializedName("creado_en") val creadoEn: String? = null
)
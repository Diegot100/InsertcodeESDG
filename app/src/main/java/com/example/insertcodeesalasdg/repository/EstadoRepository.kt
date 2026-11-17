package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.AccionRegistro
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.EventoAlerta
import com.example.insertcodeesalasdg.model.Prioridad
import com.example.insertcodeesalasdg.network.AccionRequest
import com.example.insertcodeesalasdg.network.ActualizarEventoRequest
import com.example.insertcodeesalasdg.network.EventoDto
import com.example.insertcodeesalasdg.network.SesionSupabase
import com.example.insertcodeesalasdg.network.SupabaseClient

class EstadoRepository {

    suspend fun obtenerEventos(): List<EventoAlerta> {
        return SupabaseClient.eventosApi.obtenerEventos().map { it.aEvento() }
    }

    suspend fun obtenerAcciones(): List<AccionRegistro> {
        return SupabaseClient.eventosApi.obtenerAcciones().map {
            AccionRegistro(
                id = it.id,
                eventoId = it.eventoId,
                usuario = it.usuarioEmail ?: "desconocido",
                accion = it.accion,
                fecha = (it.creadoEn ?: "").take(16).replace("T", " ")
            )
        }
    }

    suspend fun cambiarEstado(id: String, nuevoEstado: EstadoAtencion): EventoAlerta? {
        val filas = SupabaseClient.eventosApi.actualizarEvento(
            "eq.$id",
            ActualizarEventoRequest(nuevoEstado.name)
        )
        val actualizado = filas.firstOrNull() ?: return null
        val uid = SesionSupabase.usuarioId
        if (uid != null) {
            SupabaseClient.eventosApi.registrarAccion(
                AccionRequest(
                    eventoId = actualizado.id,
                    usuarioId = uid,
                    usuarioEmail = SesionSupabase.email,
                    accion = "Cambió el estado a ${nuevoEstado.name}"
                )
            )
        }
        return actualizado.aEvento()
    }

    private fun EventoDto.aEvento(): EventoAlerta {
        val prioridadEvento = Prioridad.values().find { it.name == prioridad } ?: Prioridad.BAJA
        val estado = EstadoAtencion.values().find { it.name == estadoAtencion } ?: EstadoAtencion.PENDIENTE
        return EventoAlerta(
            id = id.toString(),
            camaraId = "CAM-%02d".format(camaraId ?: 0),
            ubicacionReferencial = ubicacionReferencial,
            prioridad = prioridadEvento,
            estadoAtencion = estado,
            descripcion = descripcion
        )
    }
}
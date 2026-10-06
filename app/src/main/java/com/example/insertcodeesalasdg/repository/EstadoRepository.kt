package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.EventoAlerta
import com.example.insertcodeesalasdg.model.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EstadoRepository {

    private val _eventos = MutableStateFlow<List<EventoAlerta>>(
        listOf(
            EventoAlerta("1", "CAM-01", "Entrada principal", Prioridad.ALTA, EstadoAtencion.PENDIENTE, "Movimiento sospechoso detectado"),
            EventoAlerta("2", "CAM-02", "Estacionamiento", Prioridad.MEDIA, EstadoAtencion.ATENDIDO, "Vehículo no identificado"),
            EventoAlerta("3", "CAM-03", "Pasillo B", Prioridad.BAJA, EstadoAtencion.PENDIENTE, "Puerta abierta fuera de horario")
        )
    )
    val eventos: StateFlow<List<EventoAlerta>> = _eventos.asStateFlow()

    fun actualizarEstadoEvento(id: String, nuevoEstado: EstadoAtencion) {
        _eventos.update { lista ->
            lista.map { evento ->
                if (evento.id == id) evento.copy(estadoAtencion = nuevoEstado) else evento
            }
        }
    }
}
package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.EventoAlerta
import com.example.insertcodeesalasdg.repository.EstadoRepository
import kotlinx.coroutines.flow.StateFlow

class EstadoViewModel(
    private val repository: EstadoRepository = EstadoRepository()
) : ViewModel() {

    val eventos: StateFlow<List<EventoAlerta>> = repository.eventos

    val totalEventos: Int get() = eventos.value.size
    val pendientes: Int get() = eventos.value.count { it.estadoAtencion == EstadoAtencion.PENDIENTE }
    val atendidos: Int get() = eventos.value.count { it.estadoAtencion == EstadoAtencion.ATENDIDO }

    fun cambiarEstadoAlerta(eventoId: String, nuevoEstado: EstadoAtencion) {
        repository.actualizarEstadoEvento(eventoId, nuevoEstado)
    }
}
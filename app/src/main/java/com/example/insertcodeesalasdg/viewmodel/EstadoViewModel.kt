package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcodeesalasdg.model.AccionRegistro
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.EventoAlerta
import com.example.insertcodeesalasdg.repository.EstadoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstadoViewModel(
    private val repository: EstadoRepository = EstadoRepository()
) : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoAlerta>>(emptyList())
    val eventos: StateFlow<List<EventoAlerta>> = _eventos.asStateFlow()

    private val _acciones = MutableStateFlow<List<AccionRegistro>>(emptyList())
    val acciones: StateFlow<List<AccionRegistro>> = _acciones.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    val totalEventos: Int get() = eventos.value.size
    val pendientes: Int get() = eventos.value.count { it.estadoAtencion == EstadoAtencion.PENDIENTE }
    val atendidos: Int get() = eventos.value.count { it.estadoAtencion == EstadoAtencion.ATENDIDO }

    fun cargar() {
        if (_isLoading.value) return
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _eventos.value = repository.obtenerEventos()
                _acciones.value = repository.obtenerAcciones()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar las alertas. Revisa tu conexión e intenta de nuevo"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun cambiarEstadoAlerta(eventoId: String, nuevoEstado: EstadoAtencion) {
        viewModelScope.launch {
            try {
                val actualizado = repository.cambiarEstado(eventoId, nuevoEstado)
                if (actualizado == null) {
                    _mensaje.value = "Tu rol no tiene permiso para cambiar el estado"
                } else {
                    _eventos.update { lista ->
                        lista.map { if (it.id == eventoId) actualizado else it }
                    }
                    _acciones.value = repository.obtenerAcciones()
                    _mensaje.value = "Estado actualizado"
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mensaje.value = "No se pudo actualizar. Revisa tu conexión"
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun limpiar() {
        _eventos.value = emptyList()
        _acciones.value = emptyList()
        _error.value = null
        _mensaje.value = null
    }
}
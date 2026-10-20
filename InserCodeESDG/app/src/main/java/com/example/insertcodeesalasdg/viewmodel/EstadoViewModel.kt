package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Usuario(
    val correo: String,
    val contrasena: String,
    val rol: String = "Operador"
)

data class Evento(
    val id: Int,
    val camaraId: String,
    val ubicacionReferencial: String,
    val prioridad: String,
    val estadoAtencion: String,
    val descripcion: String
)

data class EstadoUiState(
    val usuarioAutenticado: String? = null,
    val rolUsuario: String? = null,
    val estaLogueado: Boolean = false,
    val correo: String = "",
    val contrasena: String = "",
    val usuarios: List<Usuario> = listOf(
        Usuario("admin@guardiand.cl", "123456", "Administrador"),
        Usuario("supervisor@guardiand.cl", "123456", "Supervisor"),
        Usuario("operador@guardiand.cl", "123456", "Operador")
    ),
    val eventos: List<Evento> = listOf(
        Evento(1, "CAM-01", "Entrada Principal", "Alta", "Pendiente", "Movimiento sospechoso detectado"),
        Evento(2, "CAM-02", "Estacionamiento Norte", "Media", "Atendido", "Vehículo no autorizado"),
        Evento(3, "CAM-03", "Pasillo Central", "Baja", "Pendiente", "Objeto no identificado")
    )
)

class EstadoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EstadoUiState())
    val uiState: StateFlow<EstadoUiState> = _uiState.asStateFlow()

    val eventos: List<Evento>
        get() = _uiState.value.eventos

    fun registrarUsuario(correo: String, contrasena: String, rol: String): Boolean {
        if (correo.isBlank() || contrasena.isBlank()) return false
        val yaExiste = _uiState.value.usuarios.any { it.correo.equals(correo, ignoreCase = true) }
        if (yaExiste) return false

        val nuevoUsuario = Usuario(correo = correo, contrasena = contrasena, rol = rol)
        _uiState.update { currentState ->
            currentState.copy(usuarios = currentState.usuarios + nuevoUsuario)
        }
        return true
    }

    fun validarUsuario(correo: String, contrasena: String): Boolean {
        val usuarioEncontrado = _uiState.value.usuarios.find {
            it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena
        }
        if (usuarioEncontrado != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    usuarioAutenticado = usuarioEncontrado.correo,
                    rolUsuario = usuarioEncontrado.rol,
                    estaLogueado = true,
                    correo = usuarioEncontrado.correo,
                    contrasena = usuarioEncontrado.contrasena
                )
            }
            return true
        }
        return false
    }

    fun cambiarEstadoAlerta(id: Int, nuevoEstado: String) {
        _uiState.update { currentState ->
            val listaActualizada = currentState.eventos.map { evento ->
                if (evento.id == id) evento.copy(estadoAtencion = nuevoEstado) else evento
            }
            currentState.copy(eventos = listaActualizada)
        }
    }

    fun cerrarSesion() {
        _uiState.update { currentState ->
            currentState.copy(
                usuarioAutenticado = null,
                rolUsuario = null,
                estaLogueado = false,
                correo = "",
                contrasena = ""
            )
        }
    }
}
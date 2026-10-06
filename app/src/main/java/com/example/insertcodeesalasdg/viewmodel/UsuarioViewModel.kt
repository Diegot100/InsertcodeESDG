package com.example.insertcodeesalasdg.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class UsuarioData(
    val correo: String,
    val clave: String,
    val rol: String = "Operador"
)

class UsuarioViewModel : ViewModel() {
    // Lista de usuarios de prueba iniciales con formato de correo
    val usuariosRegistrados = mutableStateListOf(
        UsuarioData("admin@guardian.cl", "1234", "Administrador"),
        UsuarioData("supervisor@guardian.cl", "1234", "Supervisor"),
        UsuarioData("operador@guardian.cl", "1234", "Operador")
    )

    private fun esCorreoValido(correo: String): Boolean {
        return correo.contains("@") && correo.contains(".") && !correo.startsWith("@") && !correo.endsWith("@")
    }

    fun registrarUsuario(correo: String, clave: String, rol: String): Pair<Boolean, String> {
        val correoLimpio = correo.trim().lowercase()

        if (correoLimpio.isBlank() || clave.isBlank()) {
            return Pair(false, "Todos los campos son obligatorios")
        }
        if (!esCorreoValido(correoLimpio)) {
            return Pair(false, "El formato del correo electrónico no es válido")
        }
        if (usuariosRegistrados.any { it.correo == correoLimpio }) {
            return Pair(false, "El correo electrónico ya está registrado")
        }

        usuariosRegistrados.add(UsuarioData(correoLimpio, clave, rol))
        return Pair(true, "Usuario registrado exitosamente")
    }

    fun validarLogin(correo: String, clave: String): Boolean {
        val correoLimpio = correo.trim().lowercase()
        return usuariosRegistrados.any {
            it.correo == correoLimpio && it.clave == clave
        }
    }
}
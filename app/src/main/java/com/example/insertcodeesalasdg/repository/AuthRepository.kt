package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.ResultadoLogin
import com.example.insertcodeesalasdg.model.Rol
import com.example.insertcodeesalasdg.model.UsuarioPrueba
import kotlinx.coroutines.delay

// Fuente de datos de autenticación simulada: no usa red real ni credenciales de la organización
class AuthRepository {

    // Si es true, el login responde como si no hubiera internet (sirve para demostrar el error de conectividad)
    var simularSinConexion: Boolean = false

    // Usuarios ficticios exigidos por la pauta; nunca credenciales reales
    private val usuarios = listOf(
        UsuarioPrueba("admin@guardian.test", "123456", Rol.ADMIN),
        UsuarioPrueba("supervisor@guardian.test", "123456", Rol.SUPERVISOR),
        UsuarioPrueba("operador@guardian.test", "123456", Rol.OPERADOR)
    )

    // suspend: se ejecuta en una corrutina para no bloquear la interfaz mientras se consulta el servicio
    suspend fun login(email: String, password: String): ResultadoLogin {
        // Simula la demora de una llamada de red para poder mostrar el estado de carga
        delay(1500)
        if (simularSinConexion) return ResultadoLogin.ErrorConexion
        // Busca al usuario ignorando mayúsculas en el correo y exigiendo la contraseña exacta
        val usuario = usuarios.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        return if (usuario != null) ResultadoLogin.Exito(usuario.rol)
        else ResultadoLogin.CredencialesInvalidas
    }
}

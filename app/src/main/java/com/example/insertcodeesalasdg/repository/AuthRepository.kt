package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.ResultadoLogin
import com.example.insertcodeesalasdg.model.Rol
import com.example.insertcodeesalasdg.model.UsuarioPrueba
import kotlinx.coroutines.delay

class AuthRepository {

    var simularSinConexion: Boolean = false

    private val usuarios = listOf(
        UsuarioPrueba("admin@guardian.test", "123456", Rol.ADMIN),
        UsuarioPrueba("supervisor@guardian.test", "123456", Rol.SUPERVISOR),
        UsuarioPrueba("operador@guardian.test", "123456", Rol.OPERADOR)
    )

    suspend fun login(email: String, password: String): ResultadoLogin {
        delay(1500)
        if (simularSinConexion) return ResultadoLogin.ErrorConexion
        val usuario = usuarios.find {
            it.email.equals(email.trim(), ignoreCase = true) && it.password == password
        }
        return if (usuario != null) ResultadoLogin.Exito(usuario.rol)
        else ResultadoLogin.CredencialesInvalidas
    }
}

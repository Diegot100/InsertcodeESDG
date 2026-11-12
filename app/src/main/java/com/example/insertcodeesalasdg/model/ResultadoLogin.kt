package com.example.insertcodeesalasdg.model

// Los tres desenlaces posibles de un intento de login: éxito con rol, credenciales inválidas o error de conexión
sealed class ResultadoLogin {
    data class Exito(val rol: Rol) : ResultadoLogin()
    data object CredencialesInvalidas : ResultadoLogin()
    data object ErrorConexion : ResultadoLogin()
}
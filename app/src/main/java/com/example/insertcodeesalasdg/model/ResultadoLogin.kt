package com.example.insertcodeesalasdg.model

sealed class ResultadoLogin {
    data class Exito(val rol: Rol) : ResultadoLogin()
    data object CredencialesInvalidas : ResultadoLogin()
    data object ErrorConexion : ResultadoLogin()
}
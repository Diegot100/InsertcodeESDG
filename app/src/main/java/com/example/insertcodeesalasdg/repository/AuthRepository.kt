package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.ResultadoLogin
import com.example.insertcodeesalasdg.model.Rol
import com.example.insertcodeesalasdg.network.IdTokenRequest
import com.example.insertcodeesalasdg.network.LoginRequest
import com.example.insertcodeesalasdg.network.SesionResponse
import com.example.insertcodeesalasdg.network.SesionSupabase
import com.example.insertcodeesalasdg.network.SupabaseClient
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {

    var simularSinConexion: Boolean = false

    suspend fun login(email: String, password: String): ResultadoLogin {
        if (simularSinConexion) return ResultadoLogin.ErrorConexion
        SesionSupabase.limpiar()
        return try {
            val sesion = SupabaseClient.api.login("password", LoginRequest(email.trim(), password))
            completarSesion(sesion)
        } catch (e: HttpException) {
            traducirHttp(e)
        } catch (e: IOException) {
            ResultadoLogin.ErrorConexion
        }
    }

    suspend fun loginConGoogle(idToken: String, nonce: String): ResultadoLogin {
        if (simularSinConexion) return ResultadoLogin.ErrorConexion
        SesionSupabase.limpiar()
        return try {
            val sesion = SupabaseClient.api.loginConIdToken(
                "id_token",
                IdTokenRequest("google", idToken, nonce)
            )
            completarSesion(sesion)
        } catch (e: HttpException) {
            traducirHttp(e)
        } catch (e: IOException) {
            ResultadoLogin.ErrorConexion
        }
    }

    private suspend fun completarSesion(sesion: SesionResponse): ResultadoLogin {
        val token = sesion.accessToken
        val uid = sesion.user?.id
        if (token == null || uid == null) return ResultadoLogin.CredencialesInvalidas
        SesionSupabase.accessToken = token
        SesionSupabase.usuarioId = uid
        SesionSupabase.email = sesion.user?.email
        val perfil = SupabaseClient.api.obtenerPerfil("eq.$uid").firstOrNull()
        val rol = Rol.values().find { it.name == perfil?.rol } ?: Rol.OPERADOR
        return ResultadoLogin.Exito(rol, sesion.user?.email)
    }

    private fun traducirHttp(e: HttpException): ResultadoLogin {
        return if (e.code() == 400 || e.code() == 401 || e.code() == 422) {
            ResultadoLogin.CredencialesInvalidas
        } else {
            ResultadoLogin.ErrorConexion
        }
    }

    fun cerrarSesion() {
        SesionSupabase.limpiar()
    }
}
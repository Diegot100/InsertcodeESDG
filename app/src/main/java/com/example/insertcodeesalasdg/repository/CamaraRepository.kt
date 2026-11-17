package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.Camara
import com.example.insertcodeesalasdg.model.EstadoCamara
import com.example.insertcodeesalasdg.network.ActualizarCamaraRequest
import com.example.insertcodeesalasdg.network.CamaraDto
import com.example.insertcodeesalasdg.network.SupabaseClient
import java.io.IOException

class CamaraRepository {

    var simularError: Boolean = false

    suspend fun obtenerCamaras(): List<Camara> {
        if (simularError) throw IOException("Sin conexión")
        return SupabaseClient.api.obtenerCamaras().map { it.aCamara() }
    }

    suspend fun actualizarGrabacion(id: Int, grabando: Boolean): Camara? {
        if (simularError) throw IOException("Sin conexión")
        val filas = SupabaseClient.api.actualizarCamara("eq.$id", ActualizarCamaraRequest(grabando))
        return filas.firstOrNull()?.aCamara()
    }

    private fun CamaraDto.aCamara(): Camara {
        val estadoCamara = EstadoCamara.values().find { it.name == estado } ?: EstadoCamara.SIN_CONEXION
        return Camara(id, nombre, zona, estadoCamara, grabando)
    }
}
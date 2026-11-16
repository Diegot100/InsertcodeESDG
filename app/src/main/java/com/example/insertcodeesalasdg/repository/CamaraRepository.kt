package com.example.insertcodeesalasdg.repository

import com.example.insertcodeesalasdg.model.Camara
import com.example.insertcodeesalasdg.model.EstadoCamara
import kotlinx.coroutines.delay
import java.io.IOException

class CamaraRepository {

    var simularError: Boolean = false

    private val camaras = listOf(
        Camara(1, "Entrada Principal", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(2, "Perímetro Norte", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(3, "Bodega A", "Zona Sur", EstadoCamara.SIN_CONEXION, false),
        Camara(4, "Estacionamiento", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(5, "Portón Vehicular", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(6, "Perímetro Este", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(7, "Almacén Central", "Zona Sur", EstadoCamara.EN_LINEA, false),
        Camara(8, "Pasillo Norte", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(9, "Pasillo Sur", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(10, "Sala de Control", "Zona Sur", EstadoCamara.EN_LINEA, false),
        Camara(11, "Andén de Carga", "Zona Sur", EstadoCamara.EN_LINEA, true),
        Camara(12, "Techumbre", "Zona Sur", EstadoCamara.MANTENCION, false)
    )

    suspend fun obtenerCamaras(): List<Camara> {
        delay(1200)
        if (simularError) throw IOException("Sin conexión")
        return camaras
    }
}
package com.example.insertcodeesalasdg.model

data class CamaraUiState(
    val isLoading: Boolean = false,
    val camaras: List<Camara> = emptyList(),
    val error: String? = null,
    val camaraSeleccionadaId: Int? = null,
    val simularError: Boolean = false,
    val mensaje: String? = null
) {
    val camaraSeleccionada: Camara?
        get() = camaras.find { it.id == camaraSeleccionadaId }

    val enLinea: Int
        get() = camaras.count { it.estado == EstadoCamara.EN_LINEA }
}
package com.example.insertcodeesalasdg.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Guía 13: guarda el Uri de la imagen elegida o capturada
class PerfilViewModel : ViewModel() {

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    fun actualizarDesdeGaleria(uri: Uri?) {
        if (uri != null) _imagenUri.value = uri // si cancela, no cambia nada
    }

    fun actualizarDesdeCamara(uri: Uri?, exito: Boolean) {
        if (exito && uri != null) _imagenUri.value = uri
    }
}

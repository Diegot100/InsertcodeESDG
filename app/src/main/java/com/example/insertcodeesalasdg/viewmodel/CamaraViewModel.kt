package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcodeesalasdg.model.CamaraUiState
import com.example.insertcodeesalasdg.repository.CamaraRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CamaraViewModel : ViewModel() {

    private val repository = CamaraRepository()

    private val _uiState = MutableStateFlow(CamaraUiState())
    val uiState: StateFlow<CamaraUiState> = _uiState.asStateFlow()

    init {
        cargarCamaras()
    }

    fun cargarCamaras() {
        if (_uiState.value.isLoading) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val lista = repository.obtenerCamaras()
                _uiState.update { it.copy(isLoading = false, camaras = lista) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "No se pudieron cargar las cámaras. Revisa tu conexión e intenta de nuevo"
                    )
                }
            }
        }
    }

    fun onSimularErrorChange(activo: Boolean) {
        repository.simularError = activo
        _uiState.update { it.copy(simularError = activo) }
        cargarCamaras()
    }

    fun seleccionar(id: Int) {
        _uiState.update { it.copy(camaraSeleccionadaId = id) }
    }

    fun alternarGrabacion(id: Int) {
        _uiState.update { estado ->
            estado.copy(
                camaras = estado.camaras.map {
                    if (it.id == id) it.copy(grabando = !it.grabando) else it
                }
            )
        }
    }
}
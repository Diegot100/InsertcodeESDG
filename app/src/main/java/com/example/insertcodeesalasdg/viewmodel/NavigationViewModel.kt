package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import com.example.insertcodeesalasdg.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationViewModel : ViewModel() {

    // Canal de eventos de navegación: la UI los escucha y ejecuta, sin que el ViewModel conozca el NavController
    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    // Pide navegar a una ruta; con limpiarPila = true se borra el historial (por ejemplo al entrar o salir de sesión)
    fun navigateTo(route: String, limpiarPila: Boolean = false) {
        _events.tryEmit(NavigationEvent.NavigateTo(route, limpiarPila))
    }

    fun navigateBack() {
        _events.tryEmit(NavigationEvent.NavigateBack)
    }
}
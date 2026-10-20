package com.example.insertcodeesalasdg.viewmodel

import androidx.lifecycle.ViewModel
import com.example.insertcodeesalasdg.navigation.NavigationEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// Guía 10: emite los eventos de navegación para que la UI los ejecute
class NavigationViewModel : ViewModel() {

    private val _events = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    fun navigateTo(route: String, limpiarPila: Boolean = false) {
        _events.tryEmit(NavigationEvent.NavigateTo(route, limpiarPila))
    }

    fun popBackStack() {
        _events.tryEmit(NavigationEvent.PopBackStack)
    }
}

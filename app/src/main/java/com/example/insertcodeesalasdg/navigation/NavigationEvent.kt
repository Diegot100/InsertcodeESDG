package com.example.insertcodeesalasdg.navigation

// Guía 10: eventos de navegación
sealed class NavigationEvent {
    data class NavigateTo(val route: String, val limpiarPila: Boolean = false) : NavigationEvent()
    object PopBackStack : NavigationEvent()
}

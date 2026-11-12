package com.example.insertcodeesalasdg.navigation

// Eventos posibles: ir a una ruta (opcionalmente limpiando la pila) o volver atrás
sealed class NavigationEvent {
    data class NavigateTo(val route: String, val limpiarPila: Boolean = false) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}
package com.example.insertcodeesalasdg.navigation

sealed class NavigationEvent {
    data class NavigateTo(val route: String, val limpiarPila: Boolean = false) : NavigationEvent()
    object NavigateBack : NavigationEvent()
}
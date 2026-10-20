package com.example.insertcodeesalasdg.navigation

sealed class AppRoutes(val route: String) {
    object Login : AppRoutes("login")
    object Home : AppRoutes("home")
    object Registro : AppRoutes("registro")
    object Resumen : AppRoutes("resumen")
    object Estado : AppRoutes("estado")
    object Perfil : AppRoutes("perfil")
}
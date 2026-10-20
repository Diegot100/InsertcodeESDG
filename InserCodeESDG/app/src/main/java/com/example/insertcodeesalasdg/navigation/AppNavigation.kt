package com.example.insertcodeesalasdg.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.insertcodeesalasdg.ui.screens.EstadoScreen
import com.example.insertcodeesalasdg.ui.screens.HomeScreen
import com.example.insertcodeesalasdg.ui.screens.LoginScreen
import com.example.insertcodeesalasdg.ui.screens.RegistroScreen
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    estadoViewModel: EstadoViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                navController = navController,
                estadoViewModel = estadoViewModel,
                onLoginExitoso = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onIrARegistro = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            RegistroScreen(
                navController = navController,
                estadoViewModel = estadoViewModel,
                onRegistroExitoso = {
                    navController.navigate("login") {
                        popUpTo("registro") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                navController = navController,
                totalEventos = 10,
                pendientes = 3,
                atendidos = 7,
                estadoViewModel = estadoViewModel,
                onVerEventosClick = {
                    navController.navigate("eventos")
                }
            )
        }

        composable("eventos") {
            EstadoScreen(
                navController = navController,
                estadoViewModel = estadoViewModel
            )
        }
    }
}
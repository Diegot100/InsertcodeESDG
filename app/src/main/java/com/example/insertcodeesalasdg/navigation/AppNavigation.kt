package com.example.insertcodeesalasdg.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.insertcodeesalasdg.ui.screens.EstadoScreen
import com.example.insertcodeesalasdg.ui.screens.HomeScreen
import com.example.insertcodeesalasdg.ui.screens.LoginScreen
import com.example.insertcodeesalasdg.ui.screens.PerfilScreen
import com.example.insertcodeesalasdg.ui.screens.RegistroScreen
import com.example.insertcodeesalasdg.ui.screens.ResumenScreen
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel
import com.example.insertcodeesalasdg.viewmodel.PerfilViewModel
import com.example.insertcodeesalasdg.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    estadoViewModel: EstadoViewModel,
    usuarioViewModel: UsuarioViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Login.route
    ) {
        composable(AppRoutes.Login.route) {
            LoginScreen(
                usuarioViewModel = usuarioViewModel,
                onLoginExitoso = {
                    navController.navigate(AppRoutes.Home.route) {
                        popUpTo(AppRoutes.Login.route) { inclusive = true }
                    }
                },
                onIrARegistro = {
                    navController.navigate(AppRoutes.Registro.route)
                }
            )
        }
        composable(AppRoutes.Home.route) {
            HomeScreen(
                navController = navController,
                totalEventos = estadoViewModel.totalEventos,
                pendientes = estadoViewModel.pendientes,
                atendidos = estadoViewModel.atendidos,
                onVerEventosClick = { navController.navigate(AppRoutes.Estado.route) }
            )
        }
        composable(AppRoutes.Registro.route) {
            RegistroScreen(
                viewModel = usuarioViewModel,
                onRegistroExitoso = {
                    navController.navigate(AppRoutes.Login.route) {
                        popUpTo(AppRoutes.Registro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoutes.Resumen.route) {
            ResumenScreen()
        }
        composable(AppRoutes.Estado.route) {
            EstadoScreen(
                viewModel = estadoViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
        composable(AppRoutes.Perfil.route) {
            PerfilScreen(
                viewModel = perfilViewModel
            )
        }
    }
}
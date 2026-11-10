package com.example.insertcodeesalasdg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.insertcodeesalasdg.navigation.AppRoutes
import com.example.insertcodeesalasdg.navigation.NavigationEvent
import com.example.insertcodeesalasdg.ui.screens.EstadoScreen
import com.example.insertcodeesalasdg.ui.screens.HomeScreen
import com.example.insertcodeesalasdg.ui.screens.LoginScreen
import com.example.insertcodeesalasdg.ui.screens.PerfilScreen
import com.example.insertcodeesalasdg.ui.screens.RegistroScreen
import com.example.insertcodeesalasdg.ui.screens.ResumenScreen
import com.example.insertcodeesalasdg.ui.theme.InserCodeTheme
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel
import com.example.insertcodeesalasdg.viewmodel.NavigationViewModel
import com.example.insertcodeesalasdg.viewmodel.PerfilViewModel
import com.example.insertcodeesalasdg.viewmodel.UsuarioViewModel
import com.example.insertcodeesalasdg.viewmodel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val estadoViewModel: EstadoViewModel by viewModels()
    private val usuarioViewModel: UsuarioViewModel by viewModels()
    private val perfilViewModel: PerfilViewModel by viewModels()
    private val navigationViewModel: NavigationViewModel by viewModels()

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InserCodeTheme {
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    navigationViewModel.events.collect { evento ->
                        when (evento) {
                            is NavigationEvent.NavigateTo -> navController.navigate(evento.route) {
                                if (evento.limpiarPila) {
                                    popUpTo(navController.graph.id) { inclusive = true }
                                }
                            }
                            NavigationEvent.NavigateBack -> navController.popBackStack()
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Login.route
                ) {
                    composable(AppRoutes.Login.route) {
                        LoginScreen(
                            loginViewModel = loginViewModel,
                            onLoginExitoso = {
                                navigationViewModel.navigateTo(AppRoutes.Home.route, limpiarPila = true)
                            }
                        )
                    }
                    composable(AppRoutes.Home.route) {
                        HomeScreen(
                            loginViewModel = loginViewModel,
                            estadoViewModel = estadoViewModel,
                            onNavegar = { ruta -> navigationViewModel.navigateTo(ruta) },
                            onCerrarSesion = {
                                loginViewModel.cerrarSesion()
                                navigationViewModel.navigateTo(AppRoutes.Login.route, limpiarPila = true)
                            }
                        )
                    }
                    composable(AppRoutes.Registro.route) {
                        RegistroScreen(
                            viewModel = usuarioViewModel,
                            onRegistroExitoso = { navigationViewModel.navigateBack() }
                        )
                    }
                    composable(AppRoutes.Resumen.route) {
                        ResumenScreen()
                    }
                    composable(AppRoutes.Estado.route) {
                        EstadoScreen(
                            viewModel = estadoViewModel,
                            onVolver = { navigationViewModel.navigateBack() }
                        )
                    }
                    composable(AppRoutes.Perfil.route) {
                        PerfilScreen(viewModel = perfilViewModel)
                    }
                }
            }
        }
    }
}
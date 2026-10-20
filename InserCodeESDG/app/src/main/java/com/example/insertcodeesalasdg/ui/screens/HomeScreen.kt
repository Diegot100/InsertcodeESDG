package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.insertcodeesalasdg.ui.components.MenuBotones
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    totalEventos: Int,
    pendientes: Int,
    atendidos: Int,
    estadoViewModel: EstadoViewModel,
    onVerEventosClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            MenuBotones(
                onIrA = { ruta -> navController.navigate(ruta) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Modo Guardián: Panel de Control",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(modifier = Modifier.weight(1f).padding(4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Eventos", style = MaterialTheme.typography.bodySmall)
                        Text("$totalEventos", style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Card(modifier = Modifier.weight(1f).padding(4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Pendientes", style = MaterialTheme.typography.bodySmall)
                        Text("$pendientes", style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Card(modifier = Modifier.weight(1f).padding(4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Atendidos", style = MaterialTheme.typography.bodySmall)
                        Text("$atendidos", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVerEventosClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gestión de Alertas y Cámaras")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    estadoViewModel.cerrarSesion()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Cerrar Sesión")
            }
        }
    }
}
package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.insertcodeesalasdg.ui.components.MenuBotones

@Composable
fun HomeScreen(
    navController: NavHostController,
    totalEventos: Int,
    pendientes: Int,
    atendidos: Int,
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
        }
    }
}
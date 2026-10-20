package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreenExpandida(
    navController: NavHostController,
    totalEventos: Int,
    pendientes: Int,
    atendidos: Int,
    onVerEventosClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Columna izquierda: Resumen de eventos
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Modo Guardián: Panel de Control",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Eventos", style = MaterialTheme.typography.bodyMedium)
                    Text("$totalEventos", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pendientes", style = MaterialTheme.typography.bodyMedium)
                    Text("$pendientes", style = MaterialTheme.typography.headlineSmall)
                }
            }

            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Atendidos", style = MaterialTheme.typography.bodyMedium)
                    Text("$atendidos", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        // Columna derecha: Accesos y gestión
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onVerEventosClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Gestión de Alertas y Cámaras")
            }
        }
    }
}
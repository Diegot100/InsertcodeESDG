package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.insertcodeesalasdg.ui.components.MenuBotones
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

@Composable
fun EstadoScreen(
    navController: NavHostController,
    estadoViewModel: EstadoViewModel
) {
    val uiState by estadoViewModel.uiState.collectAsState()

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
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Eventos y Alertas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.eventos) { evento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Cámara: ${evento.camaraId}", style = MaterialTheme.typography.titleMedium)
                            Text("Ubicación: ${evento.ubicacionReferencial}", style = MaterialTheme.typography.bodyMedium)
                            Text("Prioridad: ${evento.prioridad}", style = MaterialTheme.typography.bodySmall)
                            Text("Estado: ${evento.estadoAtencion}", style = MaterialTheme.typography.bodySmall)
                            Text("Descripción: ${evento.descripcion}", style = MaterialTheme.typography.bodySmall)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    onClick = {
                                        val nuevoEstado = if (evento.estadoAtencion == "Atendido") "Pendiente" else "Atendido"
                                        estadoViewModel.cambiarEstadoAlerta(evento.id, nuevoEstado)
                                    }
                                ) {
                                    Text(if (evento.estadoAtencion == "Atendido") "Marcar Pendiente" else "Marcar Atendido")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
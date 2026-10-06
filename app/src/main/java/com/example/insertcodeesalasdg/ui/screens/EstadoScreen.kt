package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

@Composable
fun EstadoScreen(
    viewModel: EstadoViewModel,
    onVolver: () -> Unit = {}
) {
    val eventos by viewModel.eventos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onVolver) {
            Text("Volver al Inicio")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Monitoreo de Alertas",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(eventos) { evento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = "Cámara: ${evento.camaraId}", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Ubicación: ${evento.ubicacionReferencial}")
                        Text(text = "Prioridad: ${evento.prioridad}")
                        Text(text = "Estado: ${evento.estadoAtencion}")
                        Text(text = evento.descripcion)

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.cambiarEstadoAlerta(evento.id, EstadoAtencion.ATENDIDO) },
                            enabled = evento.estadoAtencion != EstadoAtencion.ATENDIDO
                        ) {
                            Text("Marcar como Atendido")
                        }
                    }
                }
            }
        }
    }
}
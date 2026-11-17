package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.model.AccionRegistro
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.EventoAlerta
import com.example.insertcodeesalasdg.model.Prioridad
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel

@Composable
fun EstadoScreen(
    viewModel: EstadoViewModel,
    onVolver: () -> Unit = {}
) {
    val eventos by viewModel.eventos.collectAsState()
    val acciones by viewModel.acciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.cargar()
    }

    LaunchedEffect(mensaje) {
        val texto = mensaje
        if (texto != null) {
            snackbarHostState.showSnackbar(texto)
            viewModel.limpiarMensaje()
        }
    }

    val mensajeError = error
    val pendientes = eventos.count { it.estadoAtencion == EstadoAtencion.PENDIENTE }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(onClick = onVolver) { Text("← Volver al Inicio") }
                    Text(
                        text = "Alertas y eventos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$pendientes pendientes de ${eventos.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isLoading && eventos.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (mensajeError != null) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
                        Button(onClick = viewModel::cargar) { Text("Reintentar") }
                    }
                }
            }

            items(eventos, key = { it.id }) { evento ->
                TarjetaEvento(
                    evento = evento,
                    onCambiar = { nuevo -> viewModel.cambiarEstadoAlerta(evento.id, nuevo) }
                )
            }

            item {
                Text(
                    text = "Historial de acciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (acciones.isEmpty()) {
                item {
                    Text(
                        text = "Aún no hay acciones registradas.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(acciones, key = { it.id }) { accion ->
                FilaAccion(accion)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun TarjetaEvento(
    evento: EventoAlerta,
    onCambiar: (EstadoAtencion) -> Unit
) {
    val colorPrioridad = when (evento.prioridad) {
        Prioridad.ALTA -> MaterialTheme.colorScheme.error
        Prioridad.MEDIA -> Color(0xFFB45309)
        Prioridad.BAJA -> Color(0xFF15803D)
    }
    val etiquetaEstado = when (evento.estadoAtencion) {
        EstadoAtencion.PENDIENTE -> "Pendiente"
        EstadoAtencion.EN_PROCESO -> "En proceso"
        EstadoAtencion.ATENDIDO -> "Atendido"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = evento.camaraId,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "● ${evento.prioridad.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = colorPrioridad,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(text = evento.descripcion, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "${evento.ubicacionReferencial} · $etiquetaEstado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onCambiar(EstadoAtencion.EN_PROCESO) },
                    enabled = evento.estadoAtencion == EstadoAtencion.PENDIENTE,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("En proceso")
                }
                Button(
                    onClick = { onCambiar(EstadoAtencion.ATENDIDO) },
                    enabled = evento.estadoAtencion != EstadoAtencion.ATENDIDO,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Atendido")
                }
            }
        }
    }
}

@Composable
private fun FilaAccion(accion: AccionRegistro) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "${accion.accion} · evento #${accion.eventoId}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${accion.usuario} · ${accion.fecha}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.model.Camara
import com.example.insertcodeesalasdg.model.EstadoCamara
import com.example.insertcodeesalasdg.viewmodel.CamaraViewModel

@Composable
fun CamarasScreen(
    viewModel: CamaraViewModel,
    onAbrirCamara: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val mensajeError = estado.error

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onVolver) { Text("← Volver al Inicio") }
            Text(
                text = "Cámaras",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${estado.enLinea} de ${estado.camaras.size} en línea",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Simular error de red",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = estado.simularError,
                    onCheckedChange = viewModel::onSimularErrorChange
                )
            }

            when {
                estado.isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                mensajeError != null -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = mensajeError, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::cargarCamaras) { Text("Reintentar") }
                }
                else -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(estado.camaras) { camara ->
                        TarjetaCamara(camara = camara, onClick = { onAbrirCamara(camara.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaCamara(camara: Camara, onClick: () -> Unit) {
    val enLinea = camara.estado == EstadoCamara.EN_LINEA
    val colorEstado = when (camara.estado) {
        EstadoCamara.EN_LINEA -> Color(0xFF15803D)
        EstadoCamara.SIN_CONEXION -> MaterialTheme.colorScheme.error
        EstadoCamara.MANTENCION -> Color(0xFFB45309)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enLinea) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF0F172A))
                .padding(8.dp)
        ) {
            Text(
                text = if (camara.grabando) "● REC" else if (enLinea) "EN PAUSA" else "OFFLINE",
                color = if (camara.grabando) Color(0xFFF87171) else Color(0xFFCBD5E1),
                style = MaterialTheme.typography.labelSmall
            )
        }
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = camara.nombre,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = camara.zona,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "● ${camara.estado.etiqueta}",
                style = MaterialTheme.typography.bodySmall,
                color = colorEstado,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
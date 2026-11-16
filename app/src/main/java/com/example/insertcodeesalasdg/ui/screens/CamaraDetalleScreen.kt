package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.viewmodel.CamaraViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun CamaraDetalleScreen(
    viewModel: CamaraViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsState()
    val camara = estado.camaraSeleccionada
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var luzActiva by rememberSaveable { mutableStateOf(false) }
    var hablando by rememberSaveable { mutableStateOf(false) }
    var segundos by rememberSaveable { mutableStateOf(0) }
    var ahora by remember { mutableStateOf(LocalDateTime.now()) }
    val grabando = camara?.grabando == true

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            ahora = LocalDateTime.now()
        }
    }

    LaunchedEffect(grabando) {
        while (grabando) {
            delay(1000)
            segundos++
        }
    }

    val formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val tiempoRec = String.format("%02d:%02d:%02d", segundos / 3600, (segundos % 3600) / 60, segundos % 60)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        if (camara == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                TextButton(onClick = onVolver) { Text("← Volver") }
                Text("No hay una cámara seleccionada.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextButton(onClick = onVolver) { Text("← Volver a Cámaras") }
                Text(
                    text = camara.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "EN VIVO · HD",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            if (luzActiva) Color(0xFF14532D) else Color(0xFF0B2A1B),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = "INSERCODE CAM-%02d\n%s".format(camara.id, ahora.format(formato)),
                        color = Color(0xFF4ADE80),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Text(
                        text = "IR · AUTO",
                        color = Color(0xFF4ADE80),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                    Text(
                        text = if (grabando) "● REC $tiempoRec" else "EN PAUSA",
                        color = if (grabando) Color(0xFFF87171) else Color(0xFF4ADE80),
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Foto guardada (simulado)") }
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) { Text("Foto") }

                    Button(
                        onClick = { viewModel.alternarGrabacion(camara.id) },
                        colors = if (grabando) {
                            ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        } else {
                            ButtonDefaults.buttonColors()
                        },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) { Text(if (grabando) "Parar" else "Grabar") }

                    OutlinedButton(
                        onClick = { hablando = !hablando },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) { Text(if (hablando) "Hablando" else "Hablar") }

                    OutlinedButton(
                        onClick = { luzActiva = !luzActiva },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) { Text(if (luzActiva) "Luz on" else "Luz") }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Estado del dispositivo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        FilaEstado("Estado", camara.estado.etiqueta)
                        FilaEstado("Señal", "Excelente")
                        FilaEstado("Grabación", if (grabando) "Nube + Local" else "Detenida")
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaEstado(etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
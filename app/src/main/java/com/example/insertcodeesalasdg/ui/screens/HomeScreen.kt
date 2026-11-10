package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.model.EstadoAtencion
import com.example.insertcodeesalasdg.model.Rol
import com.example.insertcodeesalasdg.navigation.AppRoutes
import com.example.insertcodeesalasdg.ui.components.MenuBotones
import com.example.insertcodeesalasdg.viewmodel.EstadoViewModel
import com.example.insertcodeesalasdg.viewmodel.LoginViewModel

@Composable
fun HomeScreen(
    loginViewModel: LoginViewModel,
    estadoViewModel: EstadoViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val loginState by loginViewModel.uiState.collectAsState()
    val eventos by estadoViewModel.eventos.collectAsState()

    val total = eventos.size
    val pendientes = eventos.count { it.estadoAtencion == EstadoAtencion.PENDIENTE }
    val atendidos = eventos.count { it.estadoAtencion == EstadoAtencion.ATENDIDO }
    val avance = if (total == 0) 0 else atendidos * 100 / total

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { MenuBotones(onIrA = onNavegar) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EncabezadoLogin()
            Text(
                text = "Hola, ${loginState.rol?.etiqueta ?: ""}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = loginState.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            when (loginState.rol) {
                Rol.ADMIN -> PanelRol(
                    titulo = "Panel de Administrador",
                    descripcion = "Gestiona usuarios de prueba, roles y permisos.",
                    indicadores = listOf(
                        "Total eventos" to "$total",
                        "Pendientes" to "$pendientes",
                        "Atendidos" to "$atendidos"
                    ),
                    detalles = listOf(
                        "Admin: gestiona usuarios y permisos",
                        "Supervisor: revisa indicadores y seguimiento",
                        "Operador: atiende alertas y registra acciones"
                    ),
                    textoBoton = "Ver alertas y eventos",
                    onBoton = { onNavegar(AppRoutes.Estado.route) }
                )
                Rol.SUPERVISOR -> PanelRol(
                    titulo = "Panel de Supervisor",
                    descripcion = "Revisa estados, eventos e indicadores y da seguimiento a su atención.",
                    indicadores = listOf(
                        "Total" to "$total",
                        "Pendientes" to "$pendientes",
                        "Avance" to "$avance%"
                    ),
                    detalles = emptyList(),
                    textoBoton = "Revisar seguimiento",
                    onBoton = { onNavegar(AppRoutes.Estado.route) }
                )
                Rol.OPERADOR -> PanelRol(
                    titulo = "Panel de Operador",
                    descripcion = "Consulta cámaras, revisa eventos y registra acciones de atención.",
                    indicadores = listOf(
                        "Pendientes" to "$pendientes",
                        "Atendidos" to "$atendidos"
                    ),
                    detalles = emptyList(),
                    textoBoton = "Atender alertas",
                    onBoton = { onNavegar(AppRoutes.Estado.route) }
                )
                null -> Unit
            }

            OutlinedButton(
                onClick = onCerrarSesion,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun PanelRol(
    titulo: String,
    descripcion: String,
    indicadores: List<Pair<String, String>>,
    detalles: List<String>,
    textoBoton: String,
    onBoton: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            indicadores.forEach { (nombre, valor) ->
                TarjetaIndicador(nombre, valor, Modifier.weight(1f))
            }
        }
        if (detalles.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    detalles.forEach { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                }
            }
        }
        Button(
            onClick = onBoton,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(textoBoton)
        }
    }
}

@Composable
private fun TarjetaIndicador(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
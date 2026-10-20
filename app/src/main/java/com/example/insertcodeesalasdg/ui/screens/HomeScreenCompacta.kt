package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.R
import com.example.insertcodeesalasdg.ui.components.MenuBotones

// Guía 8 y 9: versión para pantallas pequeñas (celular)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(onIrA: (String) -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "InserCode · Ventas y Distribución") })
        }
    ) { innerPadding ->
        // Columna principal con espaciado uniforme
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "¡Bienvenido!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Gestión móvil comercial y logística",
                style = MaterialTheme.typography.bodyMedium
            )
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            MenuBotones(onIrA = onIrA)
        }
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewCompact() {
    HomeScreenCompacta()
}

package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.R
import com.example.insertcodeesalasdg.ui.components.MenuBotones

// Guía 9: versión para pantallas medianas (tablet pequeña / celular horizontal): imagen al lado del texto
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenMediana(onIrA: (String) -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "InserCode · Ventas y Distribución") })
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier.size(220.dp),
                contentScale = ContentScale.Fit
            )
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "¡Bienvenido!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Gestión móvil comercial y logística",
                    style = MaterialTheme.typography.bodyLarge
                )
                MenuBotones(onIrA = onIrA)
            }
        }
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun PreviewMedium() {
    HomeScreenMediana()
}

package com.example.insertcodeesalasdg.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.navigation.AppRoutes

@Composable
fun MenuBotones(
    onIrA: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Button(
            onClick = { onIrA(AppRoutes.Resumen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Modo especial")
        }

        Button(
            onClick = { onIrA(AppRoutes.Perfil.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text("Mi perfil")
        }
    }
}
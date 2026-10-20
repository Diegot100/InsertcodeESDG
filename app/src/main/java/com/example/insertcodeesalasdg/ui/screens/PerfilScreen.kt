package com.example.insertcodeesalasdg.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.insertcodeesalasdg.ui.components.ImagenInteligente
import com.example.insertcodeesalasdg.ui.utils.crearUriImagen
import com.example.insertcodeesalasdg.viewmodel.PerfilViewModel

// Guía 13: perfil con imagen desde galería o cámara
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(viewModel: PerfilViewModel) {
    val context = LocalContext.current
    val imagenUri by viewModel.imagenUri.collectAsState()

    // Uri temporal donde la cámara guarda la foto
    var uriTemporal by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.actualizarDesdeGaleria(uri)
    }

    // Cámara
    val camaraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { exito ->
        viewModel.actualizarDesdeCamara(uriTemporal, exito)
    }

    fun abrirCamara() {
        val uri = crearUriImagen(context)
        uriTemporal = uri
        camaraLauncher.launch(uri)
    }

    // Permiso de cámara en tiempo de ejecución
    val permisoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            abrirCamara()
        } else {
            Toast.makeText(context, "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi perfil") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {
            ImagenInteligente(uri = imagenUri)

            Button(onClick = { galeriaLauncher.launch("image/*") }) {
                Text("Elegir de la galería")
            }
            Button(onClick = {
                val permiso = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permiso == PackageManager.PERMISSION_GRANTED) {
                    abrirCamara()
                } else {
                    permisoLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text("Tomar foto")
            }
        }
    }
}

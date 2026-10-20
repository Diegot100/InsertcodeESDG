package com.example.insertcodeesalasdg.ui.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Guía 13: crea un archivo temporal en cache/images y devuelve su Uri (FileProvider)
fun crearUriImagen(context: Context): Uri {
    val carpeta = File(context.cacheDir, "images").apply { mkdirs() }
    val archivo = File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        archivo
    )
}

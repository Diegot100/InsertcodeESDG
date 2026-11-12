package com.example.insertcodeesalasdg.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.sesionDataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion_guardian")

data class PreferenciasSesion(
    val recordarme: Boolean,
    val email: String
)

class SesionPreferences(private val context: Context) {

    private val claveRecordarme = booleanPreferencesKey("recordarme")
    private val claveEmail = stringPreferencesKey("email_recordado")

    val preferencias: Flow<PreferenciasSesion> = context.sesionDataStore.data
        .catch { error ->
            if (error is IOException) emit(emptyPreferences()) else throw error
        }
        .map { datos ->
            PreferenciasSesion(
                recordarme = datos[claveRecordarme] ?: false,
                email = datos[claveEmail] ?: ""
            )
        }

    suspend fun guardar(recordarme: Boolean, email: String) {
        context.sesionDataStore.edit { datos ->
            datos[claveRecordarme] = recordarme
            if (recordarme) datos[claveEmail] = email else datos.remove(claveEmail)
        }
    }
}
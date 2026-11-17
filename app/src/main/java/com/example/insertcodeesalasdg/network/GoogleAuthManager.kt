package com.example.insertcodeesalasdg.network

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.insertcodeesalasdg.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.MessageDigest
import java.util.UUID

sealed class ResultadoGoogle {
    data class Exito(val idToken: String, val nonce: String) : ResultadoGoogle()
    data object Cancelado : ResultadoGoogle()
    data class Error(val mensaje: String) : ResultadoGoogle()
}

object GoogleAuthManager {

    suspend fun obtenerCredencial(context: Context): ResultadoGoogle {
        if (BuildConfig.GOOGLE_WEB_CLIENT_ID.isBlank()) {
            return ResultadoGoogle.Error("Falta GOOGLE_WEB_CLIENT_ID en local.properties")
        }

        val nonce = UUID.randomUUID().toString()
        val nonceHash = MessageDigest.getInstance("SHA-256")
            .digest(nonce.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val opcion = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
            .setNonce(nonceHash)
            .build()

        val solicitud = GetCredentialRequest.Builder()
            .addCredentialOption(opcion)
            .build()

        return try {
            val respuesta = CredentialManager.create(context).getCredential(context, solicitud)
            val credencial = GoogleIdTokenCredential.createFrom(respuesta.credential.data)
            ResultadoGoogle.Exito(credencial.idToken, nonce)
        } catch (e: GetCredentialCancellationException) {
            ResultadoGoogle.Cancelado
        } catch (e: NoCredentialException) {
            ResultadoGoogle.Error("No hay cuentas de Google en este dispositivo")
        } catch (e: GetCredentialException) {
            ResultadoGoogle.Error("No se pudo obtener la cuenta de Google")
        } catch (e: GoogleIdTokenParsingException) {
            ResultadoGoogle.Error("La respuesta de Google no es válida")
        }
    }
}
package com.example.insertcodeesalasdg.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcodeesalasdg.model.LoginUiState
import com.example.insertcodeesalasdg.model.ResultadoLogin
import com.example.insertcodeesalasdg.repository.AuthRepository
import com.example.insertcodeesalasdg.repository.SesionPreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel del Login: concentra la lógica y el estado; la pantalla solo dibuja y reenvía eventos
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AuthRepository()
    private val sesionPreferences = SesionPreferences(application)

    // Estado mutable y privado; hacia afuera se expone solo lectura para que la UI no lo modifique
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Interruptor de demostración para simular una caída de conexión
    private val _sinConexion = MutableStateFlow(false)
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

    // Al crear el ViewModel recupera el correo recordado, si lo hay
    init {
        cargarPreferencias()
    }

    // Lee DataStore y, si el usuario marcó Recordarme, rellena el correo
    private fun cargarPreferencias() {
        viewModelScope.launch {
            try {
                val guardadas = sesionPreferences.preferencias.first()
                if (guardadas.recordarme) {
                    _uiState.update { it.copy(email = guardadas.email, recordarme = true) }
                }
            } catch (e: CancellationException) {
                // Una cancelación de la corrutina no es un error, por eso se vuelve a lanzar
                throw e
            } catch (e: Exception) {
            }
        }
    }

    // Al escribir, actualiza el campo y borra los errores anteriores de ese campo
    fun onEmailChange(valor: String) {
        _uiState.update {
            it.copy(email = valor, errores = it.errores.copy(email = null, general = null))
        }
    }

    fun onPasswordChange(valor: String) {
        _uiState.update {
            it.copy(password = valor, errores = it.errores.copy(password = null, general = null))
        }
    }

    fun onRecordarmeChange(valor: Boolean) {
        _uiState.update { it.copy(recordarme = valor) }
    }

    fun onSinConexionChange(activo: Boolean) {
        _sinConexion.value = activo
        repository.simularSinConexion = activo
    }

    // Valida correo y contraseña, guarda los mensajes de error en el estado y devuelve si todo está correcto
    fun validarFormulario(): Boolean {
        val estado = _uiState.value
        val errorEmail = when {
            estado.email.isBlank() -> "El correo es obligatorio"
            !Patterns.EMAIL_ADDRESS.matcher(estado.email.trim()).matches() -> "Ingresa un correo válido"
            else -> null
        }
        val errorPassword = when {
            estado.password.isBlank() -> "La contraseña es obligatoria"
            estado.password.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }
        _uiState.update {
            it.copy(errores = it.errores.copy(email = errorEmail, password = errorPassword))
        }
        return errorEmail == null && errorPassword == null
    }

    // Flujo del login: validar, mostrar carga, consultar el repositorio y publicar el resultado (rol o error)
    fun iniciarSesion() {
        if (_uiState.value.isLoading || !validarFormulario()) return
        _uiState.update {
            it.copy(isLoading = true, rol = null, errores = it.errores.copy(general = null))
        }
        viewModelScope.launch {
            val estado = _uiState.value
            // Cualquier excepción inesperada se trata como error de conexión para que la app nunca se cierre
            val resultado = try {
                repository.login(estado.email, estado.password)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ResultadoLogin.ErrorConexion
            }
            // Solo si el login fue exitoso se guarda (o se borra) el correo recordado
            if (resultado is ResultadoLogin.Exito) {
                try {
                    sesionPreferences.guardar(estado.recordarme, estado.email.trim())
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                }
            }
            // Traduce cada resultado a un cambio del estado: rol en éxito, o el mensaje de error que corresponda
            _uiState.update { actual ->
                when (resultado) {
                    is ResultadoLogin.Exito -> actual.copy(isLoading = false, rol = resultado.rol)
                    ResultadoLogin.CredencialesInvalidas -> actual.copy(
                        isLoading = false,
                        errores = actual.errores.copy(password = "Correo o contraseña incorrectos")
                    )
                    ResultadoLogin.ErrorConexion -> actual.copy(
                        isLoading = false,
                        errores = actual.errores.copy(general = "Sin conexión. Revisa tu red e intenta de nuevo")
                    )
                }
            }
        }
    }

    // Deja el estado como nuevo y vuelve a cargar el correo recordado
    fun cerrarSesion() {
        _uiState.value = LoginUiState()
        cargarPreferencias()
    }
}
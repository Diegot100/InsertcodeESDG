package com.example.insertcodeesalasdg.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insertcodeesalasdg.model.LoginUiState
import com.example.insertcodeesalasdg.model.ResultadoLogin
import com.example.insertcodeesalasdg.repository.AuthRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _sinConexion = MutableStateFlow(false)
    val sinConexion: StateFlow<Boolean> = _sinConexion.asStateFlow()

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

    fun onSinConexionChange(activo: Boolean) {
        _sinConexion.value = activo
        repository.simularSinConexion = activo
    }

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

    fun iniciarSesion() {
        if (_uiState.value.isLoading || !validarFormulario()) return
        _uiState.update {
            it.copy(isLoading = true, rol = null, errores = it.errores.copy(general = null))
        }
        viewModelScope.launch {
            val estado = _uiState.value
            val resultado = try {
                repository.login(estado.email, estado.password)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ResultadoLogin.ErrorConexion
            }
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

    fun cerrarSesion() {
        _uiState.value = LoginUiState()
    }
}
package com.example.insertcodeesalasdg.model

// Estado completo de la pantalla de Login; la UI solo lo observa y dibuja
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errores: LoginErrores = LoginErrores(),
    val rol: Rol? = null
) {
    // El botón se habilita solo con ambos campos llenos y sin una carga en curso
    val isLoginEnabled: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading
}
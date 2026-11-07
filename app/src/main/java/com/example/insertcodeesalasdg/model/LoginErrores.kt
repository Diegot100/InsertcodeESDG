package com.example.insertcodeesalasdg.model

// Un mensaje por campo (null = sin error) y uno general para fallos de credenciales o conexión
data class LoginErrores(
    val email: String? = null,
    val password: String? = null,
    val general: String? = null
) {
    val hayErroresDeCampo: Boolean
        get() = email != null || password != null
}
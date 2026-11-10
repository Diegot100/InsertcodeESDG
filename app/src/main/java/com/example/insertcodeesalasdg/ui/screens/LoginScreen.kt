package com.example.insertcodeesalasdg.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.insertcodeesalasdg.R
import com.example.insertcodeesalasdg.model.LoginUiState
import com.example.insertcodeesalasdg.model.Rol
import com.example.insertcodeesalasdg.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginExitoso: () -> Unit
) {
    val estado by loginViewModel.uiState.collectAsState()
    val sinConexion by loginViewModel.sinConexion.collectAsState()

    LaunchedEffect(estado.rol) {
        if (estado.rol != null) onLoginExitoso()
    }

    LoginContenido(
        estado = estado,
        sinConexion = sinConexion,
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onSinConexionChange = loginViewModel::onSinConexionChange,
        onIngresar = loginViewModel::iniciarSesion
    )
}

@Composable
fun LoginContenido(
    estado: LoginUiState,
    sinConexion: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSinConexionChange: (Boolean) -> Unit,
    onIngresar: () -> Unit
) {
    val formulario: @Composable (Modifier) -> Unit = { modificador ->
        FormularioLogin(
            estado = estado,
            sinConexion = sinConexion,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onSinConexionChange = onSinConexionChange,
            onIngresar = onIngresar,
            modifier = modificador
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        LoginCompacto(innerPadding, formulario)
    }
}

@Composable
private fun LoginCompacto(
    innerPadding: PaddingValues,
    formulario: @Composable (Modifier) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        EncabezadoLogin()
        formulario(Modifier)
    }
}

@Composable
fun EncabezadoLogin(
    modifier: Modifier = Modifier,
    tamanoLogo: Dp = 48.dp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Modo Guardián",
            modifier = Modifier.size(tamanoLogo)
        )
        Text(
            text = "Modo Guardián",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun FormularioLogin(
    estado: LoginUiState,
    sinConexion: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSinConexionChange: (Boolean) -> Unit,
    onIngresar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var verPassword by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Usa la cuenta asignada a tu rol",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = estado.email,
                onValueChange = onEmailChange,
                label = { Text("Correo") },
                singleLine = true,
                enabled = !estado.isLoading,
                isError = estado.errores.email != null,
                supportingText = {
                    val mensaje = estado.errores.email
                    if (mensaje != null) Text(mensaje)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                singleLine = true,
                enabled = !estado.isLoading,
                isError = estado.errores.password != null,
                supportingText = {
                    val mensaje = estado.errores.password
                    if (mensaje != null) Text(mensaje)
                },
                visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { if (estado.isLoginEnabled) onIngresar() }
                ),
                trailingIcon = {
                    TextButton(onClick = { verPassword = !verPassword }) {
                        Text(if (verPassword) "Ocultar" else "Ver")
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            val errorGeneral = estado.errores.general
            if (errorGeneral != null) {
                Text(
                    text = errorGeneral,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = onIngresar,
                enabled = estado.isLoginEnabled,
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (estado.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Ingresar")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Simular sin conexión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Switch(
                    checked = sinConexion,
                    onCheckedChange = onSinConexionChange,
                    enabled = !estado.isLoading
                )
            }

            Text(
                text = "Cuentas de prueba: " + Rol.values().joinToString(" · ") { it.etiqueta },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
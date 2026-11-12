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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.tooling.preview.Preview
import com.example.insertcodeesalasdg.model.LoginErrores
import com.example.insertcodeesalasdg.ui.theme.InserCodeTheme
import com.example.insertcodeesalasdg.ui.utils.obtenerWindowSizeClass

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginExitoso: () -> Unit
) {
    val estado by loginViewModel.uiState.collectAsState()
    val sinConexion by loginViewModel.sinConexion.collectAsState()
    val anchoPantalla = obtenerWindowSizeClass().widthSizeClass

    LaunchedEffect(estado.rol) {
        if (estado.rol != null) onLoginExitoso()
    }

    LoginContenido(
        anchoPantalla = anchoPantalla,
        estado = estado,
        sinConexion = sinConexion,
        onEmailChange = loginViewModel::onEmailChange,
        onPasswordChange = loginViewModel::onPasswordChange,
        onSinConexionChange = loginViewModel::onSinConexionChange,
        onIngresar = loginViewModel::iniciarSesion,
        onRecordarmeChange = loginViewModel::onRecordarmeChange
    )
}

@Composable
fun LoginContenido(
    anchoPantalla: WindowWidthSizeClass,
    estado: LoginUiState,
    sinConexion: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSinConexionChange: (Boolean) -> Unit,
    onIngresar: () -> Unit,
    onRecordarmeChange: (Boolean) -> Unit = {}
) {
    val formulario: @Composable (Modifier) -> Unit = { modificador ->
        FormularioLogin(
            estado = estado,
            sinConexion = sinConexion,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onSinConexionChange = onSinConexionChange,
            onIngresar = onIngresar,
            onRecordarmeChange = onRecordarmeChange,
            modifier = modificador
        )
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        when (anchoPantalla) {
            WindowWidthSizeClass.Compact -> LoginCompacto(innerPadding, formulario)
            else -> LoginAmplio(innerPadding, formulario)
        }
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
    onRecordarmeChange: (Boolean) -> Unit = {},
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.recordarme,
                    onCheckedChange = onRecordarmeChange,
                    enabled = !estado.isLoading
                )
                Text(
                    text = "Recordarme",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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

@Composable
private fun LoginAmplio(
    innerPadding: PaddingValues,
    formulario: @Composable (Modifier) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(48.dp),
        horizontalArrangement = Arrangement.spacedBy(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EncabezadoLogin(tamanoLogo = 96.dp)
            Text(
                text = "Monitoreo móvil de eventos y alertas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        formulario(
            Modifier
                .weight(1f)
                .widthIn(max = 480.dp)
        )
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewLoginCompacto() {
    InserCodeTheme {
        LoginContenido(
            anchoPantalla = WindowWidthSizeClass.Compact,
            estado = LoginUiState(email = "operador@guardian.test", password = "123456"),
            sinConexion = false,
            onEmailChange = {},
            onPasswordChange = {},
            onSinConexionChange = {},
            onIngresar = {}
        )
    }
}

@Preview(name = "Medium", widthDp = 700, heightDp = 900, showBackground = true)
@Composable
fun PreviewLoginMediano() {
    InserCodeTheme {
        LoginContenido(
            anchoPantalla = WindowWidthSizeClass.Medium,
            estado = LoginUiState(email = "operador@guardian.test", password = "123456"),
            sinConexion = false,
            onEmailChange = {},
            onPasswordChange = {},
            onSinConexionChange = {},
            onIngresar = {}
        )
    }
}

@Preview(name = "Expanded", widthDp = 1100, heightDp = 700, showBackground = true)
@Composable
fun PreviewLoginExpandido() {
    InserCodeTheme {
        LoginContenido(
            anchoPantalla = WindowWidthSizeClass.Expanded,
            estado = LoginUiState(email = "operador@guardian.test", password = "123456"),
            sinConexion = false,
            onEmailChange = {},
            onPasswordChange = {},
            onSinConexionChange = {},
            onIngresar = {}
        )
    }
}

@Preview(name = "Con errores", widthDp = 360, heightDp = 800, showBackground = true)
@Composable
fun PreviewLoginConErrores() {
    InserCodeTheme {
        LoginContenido(
            anchoPantalla = WindowWidthSizeClass.Compact,
            estado = LoginUiState(
                email = "operador@",
                password = "123",
                errores = LoginErrores(
                    email = "Ingresa un correo válido",
                    password = "Debe tener al menos 6 caracteres",
                    general = "Sin conexión. Revisa tu red e intenta de nuevo"
                )
            ),
            sinConexion = true,
            onEmailChange = {},
            onPasswordChange = {},
            onSinConexionChange = {},
            onIngresar = {}
        )
    }
}
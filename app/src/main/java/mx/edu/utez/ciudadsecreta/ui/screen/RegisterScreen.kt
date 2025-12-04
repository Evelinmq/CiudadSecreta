package mx.edu.utez.ciudadsecreta.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import mx.edu.utez.ciudadsecreta.viewmodel.RegisterViewModel


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegistrationSuccess: () -> Unit, // Callback al terminar registro
    onNavigateBack: () -> Unit
) {

    LaunchedEffect(viewModel.isRegisterSuccess) {
        if (viewModel.isRegisterSuccess) {
            onRegistrationSuccess()
            viewModel.isRegisterSuccess = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Usuario", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Nombre
        OutlinedTextField(
            value = viewModel.nombre,
            onValueChange = { viewModel.nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        // correo
        OutlinedTextField(
            value = viewModel.correo,
            onValueChange = { viewModel.correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        // Contraseña
        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Repetir Contraseña
        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = { Text("Repetir Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.password != viewModel.confirmPassword && viewModel.confirmPassword.isNotEmpty(),
            supportingText = {
                if (viewModel.password != viewModel.confirmPassword && viewModel.confirmPassword.isNotEmpty()) {
                    Text("Las contraseñas no coinciden")
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.onRegisterClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón para cancelar / volver al login manualmente
            OutlinedButton(
                onClick = { onNavigateBack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Login")
            }
        }

        // Mensajes de error/éxito
        viewModel.errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        viewModel.successMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
package mx.edu.utez.ciudadsecreta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.repository.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var nombreUsuario by mutableStateOf("")
    var contrasena by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoginSuccess by mutableStateOf(false)

    fun onLoginClick() {
        if (nombreUsuario.isBlank() || contrasena.isBlank()) {
            errorMessage = "Ingresa correo y contraseña"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = repository.login(nombreUsuario, contrasena)
            isLoading = false

            result.onSuccess {
                isLoginSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error al iniciar sesión"
            }
        }
    }
}

package mx.edu.utez.ciudadsecreta.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.data.model.UsuarioRequest
import mx.edu.utez.ciudadsecreta.repository.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    // Estados para los campos de texto
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // Estado para la UI (Carga y mensajes)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)
    var isRegisterSuccess by mutableStateOf(false)

    fun onRegisterClick() {
        // 1. Validaciones Locales
        if (nombre.isBlank() || correo.isBlank() || password.isBlank()) {
            errorMessage = "Todos los campos son obligatorios"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden"
            return
        }

        // 2. Preparar datos y llamar al repo
        isLoading = true
        errorMessage = null
        successMessage = null
        isRegisterSuccess = false



        val newUser = UsuarioRequest(
            nombre = nombre,
            correo = correo,
            password = password
        )

        viewModelScope.launch {
            val result = repository.registrarUsuario(newUser)
            isLoading = false
            result.onSuccess { msg ->
                successMessage = msg
                limpiarFormulario()
                isRegisterSuccess = true
            }.onFailure { error ->
                errorMessage = error.message ?: "Error desconocido"
            }
        }
    }

    private fun limpiarFormulario() {
        nombre = ""
        correo = ""
        password = ""
        confirmPassword = ""

    }
}
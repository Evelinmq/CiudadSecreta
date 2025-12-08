package mx.edu.utez.ciudadsecreta.data.model

import com.google.gson.annotations.SerializedName

// Archivo: UsuarioRequest.kt (Versión Corregida)
data class UsuarioRequest (
    // CAMBIO CRÍTICO: Serializa como "nombre"
    @SerializedName(value = "nombre") val nombre: String,

    // Estos ya eran correctos y coinciden con Flask
    @SerializedName(value = "correo") val correo: String,
    @SerializedName(value = "password") val password: String
)
package mx.edu.utez.ciudadsecreta.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("password") val contrasena: String
)
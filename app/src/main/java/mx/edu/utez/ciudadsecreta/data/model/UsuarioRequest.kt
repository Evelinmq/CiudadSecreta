package mx.edu.utez.ciudadsecreta.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioRequest (

    @SerializedName("nombreUsuario") val nombreUsuario: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val contrasena: String
)
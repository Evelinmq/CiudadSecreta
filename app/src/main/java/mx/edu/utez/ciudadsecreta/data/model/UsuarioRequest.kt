package mx.edu.utez.ciudadsecreta.data.model

import com.google.gson.annotations.SerializedName

data class UsuarioRequest (

    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("password") val password: String
)
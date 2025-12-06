package mx.edu.utez.ciudadsecreta.data.model

data class PuntoRequest(
    val lat: Double,
    val lon: Double,
    val mensaje: String,
    val autor: String
)

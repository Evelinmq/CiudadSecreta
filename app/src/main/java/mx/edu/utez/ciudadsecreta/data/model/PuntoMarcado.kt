package mx.edu.utez.ciudadsecreta.data.model

data class PuntoMarcado(
    val id: Int,
    val lat: Double,
    val lon: Double,
    val mensaje: String,
    val autor: String,
    val timestamp: Long

)


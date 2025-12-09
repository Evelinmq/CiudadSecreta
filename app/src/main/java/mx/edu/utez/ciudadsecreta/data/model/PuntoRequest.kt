package mx.edu.utez.ciudadsecreta.data.model

data class PuntoRequest(
    val lat: Double,
    val lon: Double,
    val mensaje: String,
    val autor: String,
    val timestamp: Long = 0 //Luego en la vista el viewmodel deberia sustituir este valor por defecto, según
)


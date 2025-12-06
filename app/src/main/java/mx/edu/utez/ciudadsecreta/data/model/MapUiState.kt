package mx.edu.utez.ciudadsecreta.data.model

data class MapUiState(
    val currentDialog: DialogType? = null,
    val lat: Double = 0.0,
    val lon: Double = 0.0,

    val mensaje: String = "",
    val puntoSeleccionado: PuntoMarcado? = null
)

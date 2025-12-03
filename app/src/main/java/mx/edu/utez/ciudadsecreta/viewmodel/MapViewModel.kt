package mx.edu.utez.ciudadsecreta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import mx.edu.utez.ciudadsecreta.repository.PuntoRepository

// ui state para diálogos
//cambiar después de carpeta
data class MapUiState(
    //Añadir, mostrar, editar
    val showAddDialog: Boolean = false,
    val showViewDialog: Boolean = false,
    val showEditDialog: Boolean = false,

    //Ubicación
    val lat: Double = 0.0,
    val lon: Double = 0.0,

    val mensaje: String = "",
    val puntoSeleccionado: PuntoMarcado? = null
)

class MapViewModel(private val repo: PuntoRepository) : ViewModel() {

    private val usuarioActual = "" //Enlazar luego segun quien inicio sesión en el login
    private val _puntos = MutableStateFlow<List<PuntoMarcado>>(emptyList())
    val puntos = _puntos.asStateFlow()

    // Estado UI de los diálogos
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarPuntos()
    }

    fun cargarPuntos() {
        viewModelScope.launch {
            try {
                _puntos.value = repo.getPuntos()
            } catch (e: Exception) {
                println("Error cargando puntos: $e")
            }
        }
    }

    fun agregarPunto(lat: Double, lon: Double, mensaje: String) {
        viewModelScope.launch {

            val nuevo = PuntoMarcado(
                id = 0,
                lat = lat,
                lon = lon,
                mensaje = mensaje,
                autor = "Secreto",
                timestamp = System.currentTimeMillis()
            )

            try {
                repo.agregarPunto(nuevo)
                cargarPuntos() // Refrescar el mapa
            } catch (e: Exception) {
                println("Error guardando punto: $e")
            }
        }
    }

    fun actualizarPunto(punto: PuntoMarcado) {
        viewModelScope.launch {
            try {
                repo.actualizarPunto(punto)
                cargarPuntos()
            } catch (e: Exception) {
                println("Error actualizando punto: $e")
            }
        }
    }

    fun eliminarPunto(id: Int) {
        viewModelScope.launch {
            try {
                repo.eliminarPunto(id)
                cargarPuntos()
            } catch (e: Exception) {
                println("Error eliminando punto: $e")
            }
        }
    }

    // prepara el diálogo de agregar
    fun prepararNuevoRumor(lat: Double, lon: Double) {
        _uiState.value = MapUiState(
            showAddDialog = true,
            lat = lat,
            lon = lon
        )
    }

    // guarda el rumor desde el diálogo
    fun guardarRumor(texto: String) {
        val st = uiState.value
        agregarPunto(
            lat = st.lat,
            lon = st.lon,
            mensaje = texto
        )
        cerrarDialogos()
    }

    // abre diálogo para ver el rumor
    fun abrirRumor(mensaje: String) {
        _uiState.value = MapUiState(
            showViewDialog = true,
            mensaje = mensaje
        )
    }

    // Cerrar diálogo
    fun cerrarDialogos() {
        _uiState.value = MapUiState()
    }

    //muestra detalles del rumor
    fun abrirRumor(punto: PuntoMarcado) {
        _uiState.value = uiState.value.copy(
            showViewDialog = true,
            mensaje = punto.mensaje,
            puntoSeleccionado = punto
        )
    }

    //Prepara la edición
    fun prepararEdicion() {
        val punto = _uiState.value.puntoSeleccionado ?: return
        _uiState.value = uiState.value.copy(
            showViewDialog = false,
            showEditDialog = true,
            mensaje = punto.mensaje
        )
    }

    //Guardar edición del rumor
    fun editarRumor(nuevoMensaje: String) {
        val punto = _uiState.value.puntoSeleccionado ?: return

        val actualizado = punto.copy(
            mensaje = nuevoMensaje
        )

        viewModelScope.launch {
            repo.actualizarPunto(actualizado)
            cargarPuntos()
            cerrarDialogos()
        }
    }

    // Borrar rumor
    fun borrarRumor() {
        val punto = _uiState.value.puntoSeleccionado ?: return

        viewModelScope.launch {
            repo.eliminarPunto(punto.id)
            cargarPuntos()
            cerrarDialogos()
        }
    }
}
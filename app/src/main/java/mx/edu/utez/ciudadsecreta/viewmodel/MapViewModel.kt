package mx.edu.utez.ciudadsecreta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.data.model.DialogType
import mx.edu.utez.ciudadsecreta.data.model.MapUiState
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.data.model.toPuntoMarcado
import mx.edu.utez.ciudadsecreta.repository.PuntoRepository
import org.osmdroid.util.GeoPoint
import java.lang.Exception

class MapViewModel(private val repo: PuntoRepository) : ViewModel() {

    private val _puntos = MutableStateFlow<List<PuntoMarcado>>(emptyList())
    val puntos = _puntos.asStateFlow()

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarPuntos()
    }

    fun cargarPuntos() {
        viewModelScope.launch {
            try {
                val result = repo.obtenerPuntos()
                result.onSuccess { puntosList ->
                    _puntos.value = puntosList.map { it.toPuntoMarcado() }
                }.onFailure { exception ->
                    // se pueden manejar errores
                }
            } catch (e: Exception) {
                // log
            }
        }
    }

    // Abrir diálogo para agregar
    fun prepararNuevoRumor(lat: Double, lon: Double) {
        _uiState.value = MapUiState(
            currentDialog = DialogType.ADD,
            lat = lat,
            lon = lon
        )
    }

    fun abrirRumor(punto: PuntoMarcado) {
        _uiState.value = MapUiState(
            currentDialog = DialogType.VIEW,
            mensaje = punto.mensaje,
            puntoSeleccionado = punto
        )
    }

    fun prepararEdicion() {
        val punto = _uiState.value.puntoSeleccionado ?: return
        _uiState.value = _uiState.value.copy(
            currentDialog = DialogType.EDIT,
            mensaje = punto.mensaje
        )
    }

    // crearPunto que recibe PuntoRequest (tu UI lo llama así)
    fun crearPunto(req: PuntoRequest) {
        viewModelScope.launch {
            try {
                // Aseguramos timestamp en segundos
                val withTs = PuntoRequest(
                    lat = req.lat,
                    lon = req.lon,
                    mensaje = req.mensaje,
                    autor = req.autor,
                    timestamp = (System.currentTimeMillis() / 1000)
                )

                repo.crearPunto(withTs)
                    .onSuccess {
                        cargarPuntos()
                        cerrarDialogos()
                    }.onFailure {
                        // manejar error
                    }
            } catch (e: Exception) {
                // manejar
            }
        }
    }

    // cuando la UI llama guardarRumor (usa uiState.lat/uiState.lon)
    fun guardarRumor(texto: String) {
        val st = uiState.value

        viewModelScope.launch {
            try {
                val req = PuntoRequest(
                    lat = st.lat,
                    lon = st.lon,
                    mensaje = texto,
                    autor = "Secreto",
                    timestamp = (System.currentTimeMillis() / 1000)

                )

                repo.crearPunto(req)
                    .onSuccess {
                        Log.d("MapViewModel", "Punto creado con éxito. Cerrando diálogos.")
                        cargarPuntos()
                        cerrarDialogos()
                    }.onFailure { exception ->

                        Log.e("MapViewModel", "FALLO DE CREACIÓN DE PUNTO (API/Repositorio): ${exception.message}", exception)

                    }
            } catch (e: Exception) {
                Log.e("MapViewModel", "EXCEPCIÓN GENERAL EN GUARDAR: ${e.message}", e)
            }
        }
    }
    fun editarRumor(nuevoMensaje: String) {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch {
            try {
                val request = PuntoRequest(
                    lat = punto.lat,
                    lon = punto.lon,
                    mensaje = nuevoMensaje,
                    autor = punto.autor,
                    timestamp = (System.currentTimeMillis() / 1000)
                )
                repo.actualizarPunto(id = punto.id, req = request)
                    .onSuccess {
                        cargarPuntos()
                        cerrarDialogos()
                    }.onFailure {
                        // manejar
                    }
            } catch (e: Exception) {
                // manejar
            }
        }
    }

    fun borrarRumor() {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch {
            try {
                repo.eliminarPunto(punto.id)
                cargarPuntos()
                cerrarDialogos()
            } catch (e: Exception) {
                // manejar error
            }
        }
    }

    fun cerrarDialogos() {
        _uiState.value = MapUiState()
    }

    // Estado para la ubicación inicial del usuario (puede ser nulo al inicio)
    private val _userInitialLocation = MutableStateFlow<GeoPoint?>(null)
    val userInitialLocation = _userInitialLocation.asStateFlow()

    fun setInitialLocation(lat: Double, lon: Double) {
        _userInitialLocation.value = GeoPoint(lat, lon)
    }

    // Ubicación por defecto de México (CDMX)
    fun setDefaultLocation() {
        _userInitialLocation.value = GeoPoint(19.4326, -99.1332)
    }
}
package mx.edu.utez.ciudadsecreta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // Estado de carga para deshabilitar el botón
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    init {
        cargarPuntos()
    }

    fun cargarPuntos() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.obtenerPuntos()
                result.onSuccess { puntosList ->
                    withContext(Dispatchers.Main) {
                        _puntos.value = puntosList.map { it.toPuntoMarcado() }
                    }
                }.onFailure { exception ->
                    Log.e("MapViewModel", "Error al cargar puntos: ${exception.message}")
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "Excepción al cargar puntos: ${e.message}")
            }
        }
    }

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

    // Lógica de guardado con manejo de isSaving y centrado de mapa
    fun guardarRumor(texto: String) {
        val st = uiState.value

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _isSaving.value = true
            }

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
                        // 🚩 CORRECCIÓN CLAVE: Centrar el mapa en el nuevo punto para forzar el redibujado
                        withContext(Dispatchers.Main) {
                            setInitialLocation(st.lat, st.lon)
                        }
                    }.onFailure { exception ->
                        Log.e("MapViewModel", "FALLO DE CREACIÓN: ${exception.message}", exception)
                    }
            } catch (e: Exception) {
                Log.e("MapViewModel", "EXCEPCIÓN GENERAL EN GUARDAR: ${e.message}", e)
            } finally {
                // Ocultar carga y actualizar la lista de puntos
                withContext(Dispatchers.Main) {
                    _isSaving.value = false
                    cargarPuntos()
                    cerrarDialogos()
                }
            }
        }
    }

    fun editarRumor(nuevoMensaje: String) {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch(Dispatchers.IO) {
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
                        withContext(Dispatchers.Main) {
                            cargarPuntos()
                            cerrarDialogos()
                        }
                    }.onFailure { /* Manejo de errores */ }
            } catch (e: Exception) { /* Manejo de errores */ }
        }
    }

    fun borrarRumor() {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.eliminarPunto(punto.id)
                withContext(Dispatchers.Main) {
                    cargarPuntos()
                    cerrarDialogos()
                }
            } catch (e: Exception) { /* Manejo de errores */ }
        }
    }

    fun cerrarDialogos() {
        _uiState.value = MapUiState()
    }

    private val _userInitialLocation = MutableStateFlow<GeoPoint?>(null)
    val userInitialLocation = _userInitialLocation.asStateFlow()

    fun setInitialLocation(lat: Double, lon: Double) {
        _userInitialLocation.value = GeoPoint(lat, lon)
    }

    fun setDefaultLocation() {
        _userInitialLocation.value = GeoPoint(19.4326, -99.1332)
    }
}
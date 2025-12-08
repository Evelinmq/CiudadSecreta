package mx.edu.utez.ciudadsecreta.viewmodel

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
            val result = repo.obtenerPuntos()
            result.onSuccess { puntosList ->
                _puntos.value = puntosList.map { it.toPuntoMarcado() }
            }.onFailure { exception ->
            }
        }
    }


    //Abrir diálogos
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
    fun crearPunto(req: PuntoRequest) {
        viewModelScope.launch {
            repo.crearPunto(req)
                .onSuccess {
                    cargarPuntos()
                    cerrarDialogos()
                }
        }
    }


    //Guardar
    fun guardarRumor(texto: String) {
        val st = uiState.value

        viewModelScope.launch {

            repo.crearPunto(
                req = PuntoRequest(lat = st.lat, lon = st.lon, mensaje = texto,autor = texto )
            ).onSuccess {
                cargarPuntos()
                cerrarDialogos()
            }
        }
    }



    fun editarRumor(nuevoMensaje: String) {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch {
            val request = PuntoRequest(
                lat = punto.lat,
                lon = punto.lon,
                mensaje = nuevoMensaje,
                autor = punto.autor)
            repo.actualizarPunto(id = punto.id, req = request)
                .onSuccess {
                    cargarPuntos()
                    cerrarDialogos()
                }
        }
    }

    fun borrarRumor() {
        val punto = _uiState.value.puntoSeleccionado ?: return
        viewModelScope.launch {
            repo.eliminarPunto(punto.id)
            cargarPuntos()
            cerrarDialogos()
        }
    }

    fun cerrarDialogos() {
        _uiState.value = MapUiState()
    }



}

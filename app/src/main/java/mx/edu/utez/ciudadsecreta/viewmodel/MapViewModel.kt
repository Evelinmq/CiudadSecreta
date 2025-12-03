package mx.edu.utez.ciudadsecreta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import mx.edu.utez.ciudadsecreta.repository.PuntoRepository

class MapViewModel(private val repo: PuntoRepository) : ViewModel() {

    private val _puntos = MutableStateFlow<List<PuntoMarcado>>(emptyList())
    val puntos = _puntos.asStateFlow()

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
                autor = "Usuario",
                timestamp = System.currentTimeMillis()
            )

            try {
                repo.agregarPunto(nuevo)
                cargarPuntos() // Refrescar mapa
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

}

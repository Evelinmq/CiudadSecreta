package mx.edu.utez.ciudadsecreta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.data.model.PuntoResponse
import mx.edu.utez.ciudadsecreta.repository.PuntoRepository

class PuntoViewModel(private val repo: PuntoRepository) : ViewModel() {


    private val _puntos = MutableStateFlow<List<PuntoResponse>>(emptyList())
    val puntos = _puntos.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando = _cargando.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()


    fun cargarPuntos() {
        viewModelScope.launch {
            _cargando.value = true

            val result = repo.obtenerPuntos()

            if (result.isSuccess) {
                _puntos.value = result.getOrNull()!!
            } else {
                _mensaje.value = "Error al obtener puntos"
            }

            _cargando.value = false
        }
    }

    //POST -> crea un punto
    fun crearPunto(req: PuntoRequest) {
        viewModelScope.launch {
            _cargando.value = true

            val result = repo.crearPunto(req)

            if (result.isSuccess) {
                _mensaje.value = "Punto creado correctamente"
                cargarPuntos()
            } else {
                _mensaje.value = "Error al crear punto"
            }

            _cargando.value = false
        }
    }

    fun actualizarPunto(id: Int, req: PuntoRequest) {
        viewModelScope.launch {
            _cargando.value = true

            val result = repo.actualizarPunto(id, req)

            if (result.isSuccess) {
                _mensaje.value = "Punto actualizado"
                cargarPuntos()
            } else {
                _mensaje.value = "Error al actualizar punto"
            }

            _cargando.value = false
        }
    }


    //Eliminar -> DELETE
    fun eliminarPunto(id: Int) {
        viewModelScope.launch {
            _cargando.value = true

            val result = repo.eliminarPunto(id)

            if (result.isSuccess) {
                _mensaje.value = "Punto eliminado"
                cargarPuntos()
            } else {
                _mensaje.value = "Error al eliminar punto"
            }

            _cargando.value = false
        }
    }

    //Limpiar los mensajes
    fun limpiarMensaje() {
        _mensaje.value = null
    }
}

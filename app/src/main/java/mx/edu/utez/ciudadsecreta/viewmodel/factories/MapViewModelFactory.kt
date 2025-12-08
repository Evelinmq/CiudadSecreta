package mx.edu.utez.ciudadsecreta.viewmodel.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mx.edu.utez.ciudadsecreta.repository.PuntoRepository
import mx.edu.utez.ciudadsecreta.viewmodel.MapViewModel

class MapViewModelFactory(private val repo: PuntoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(repo) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}
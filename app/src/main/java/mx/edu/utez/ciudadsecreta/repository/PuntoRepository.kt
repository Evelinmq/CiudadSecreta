package mx.edu.utez.ciudadsecreta.repository

import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import mx.edu.utez.ciudadsecreta.data.retrofit.PuntoApi

class PuntoRepository(private val api: PuntoApi) {

    suspend fun getPuntos(): List<PuntoMarcado> =
        api.obtenerPuntos()

    suspend fun agregarPunto(punto: PuntoMarcado): PuntoMarcado =
        api.crearPunto(punto)

    suspend fun actualizarPunto(punto: PuntoMarcado): PuntoMarcado =
        api.actualizarPunto(punto.id, punto)

    suspend fun eliminarPunto(id: Int) =
        api.eliminarPunto(id)

}

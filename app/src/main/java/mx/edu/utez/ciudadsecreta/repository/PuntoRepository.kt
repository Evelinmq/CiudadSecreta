package mx.edu.utez.ciudadsecreta.repository

import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.data.model.PuntoResponse
import mx.edu.utez.ciudadsecreta.data.retrofit.PuntoApi

class PuntoRepository(private val api: PuntoApi) {

    suspend fun obtenerPuntos(): Result<List<PuntoResponse>> {
        return safeCall { api.obtenerPuntos() }
    }

    suspend fun crearPunto(req: PuntoRequest): Result<PuntoResponse> {
        return safeCall { api.crearPunto(req) }
    }

    suspend fun actualizarPunto(id: Int, req: PuntoRequest): Result<PuntoResponse> {
        return safeCall { api.actualizarPunto(id, req) }
    }

    suspend fun eliminarPunto(id: Int): Result<Unit> {
        return safeCall { api.eliminarPunto(id) }
    }

    private inline fun <T> safeCall(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


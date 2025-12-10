package mx.edu.utez.ciudadsecreta.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.data.model.PuntoResponse
import mx.edu.utez.ciudadsecreta.data.retrofit.PuntoApi
import java.lang.Exception

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

    // CORRECCIÓN CLAVE: withContext(Dispatchers.IO) asegura la ejecución en segundo plano
    private suspend inline fun <T> safeCall(crossinline block: suspend () -> T): Result<T> {

        return withContext(Dispatchers.IO) {
            try {
                Result.success(block())
            } catch (e: Exception) {
                Log.e("PuntoRepository", "Fallo en la llamada a la API: ${e.message}", e)
                Result.failure(e)
            }
        }
    }
}


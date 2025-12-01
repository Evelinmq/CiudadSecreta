package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

interface PuntoApi {

    @GET("puntos")
    suspend fun obtenerPuntos(): List<PuntoMarcado>

    @POST("puntos")
    suspend fun crearPunto(
        @Body punto: PuntoMarcado
    ): PuntoMarcado
}

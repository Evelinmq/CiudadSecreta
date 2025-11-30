package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado

interface PuntoApi {

    @GET("puntos")
    suspend fun obtenerPuntos(): List<PuntoMarcado>

    @POST("puntos")
    suspend fun crearPunto(
        @Body punto: PuntoMarcado
    ): PuntoMarcado
}

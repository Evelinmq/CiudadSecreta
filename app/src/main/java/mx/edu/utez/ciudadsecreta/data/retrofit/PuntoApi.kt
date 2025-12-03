package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PuntoApi {

    @GET("puntos")
    suspend fun obtenerPuntos(): List<PuntoMarcado>

    @POST("puntos")
    suspend fun crearPunto(
        @Body punto: PuntoMarcado
    ): PuntoMarcado

    @PUT("puntos/{id}")
    suspend fun actualizarPunto(
        @Path("id") id: Int,
        @Body punto: PuntoMarcado
    ): PuntoMarcado

    @DELETE("puntos/{id}")
    suspend fun eliminarPunto(
        @Path("id") id: Int
    )

}

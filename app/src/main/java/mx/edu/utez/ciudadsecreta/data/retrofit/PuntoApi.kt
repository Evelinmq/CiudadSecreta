package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.PuntoRequest
import mx.edu.utez.ciudadsecreta.data.model.PuntoResponse
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PuntoApi {

    @GET("api/puntos")
    suspend fun obtenerPuntos(): List<PuntoResponse>

    @POST("api/puntos")
    suspend fun crearPunto(
        @Body punto: PuntoRequest
    ): PuntoResponse

    @PUT("api/puntos/{id}")
    suspend fun actualizarPunto(
        @Path("id") id: Int,
        @Body punto: PuntoRequest
    ): PuntoResponse

    @DELETE("api/puntos/{id}")
    suspend fun eliminarPunto(
        @Path("id") id: Int
    )

    /*
    @GET("puntos")
    suspend fun obtenerPuntos(): List<PuntoResponse>

    @POST("puntos")
    suspend fun crearPunto(
        @Body punto: PuntoRequest
    ): PuntoResponse

    @PUT("puntos/{id}")
    suspend fun actualizarPunto(
        @Path("id") id: Int,
        @Body punto: PuntoRequest
    ): PuntoResponse

    @DELETE("puntos/{id}")
    suspend fun eliminarPunto(
        @Path("id") id: Int
    )

     */
}


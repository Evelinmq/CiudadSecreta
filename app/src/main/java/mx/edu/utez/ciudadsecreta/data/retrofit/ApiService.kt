package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.LoginRequest
import mx.edu.utez.ciudadsecreta.data.model.PuntoMarcado
import mx.edu.utez.ciudadsecreta.data.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUsuario(@Body credentials: LoginRequest): Response<LoginResponse> //Antes <void>
    @POST("api/auth/register")
    suspend fun registrarUsuario(@Body user: UsuarioRequest): Response<RegisterResponse> //Antes <void>


    data class LoginResponse(
        val token: String,
        val usuario: String
    )

    data class RegisterResponse(
        val mensaje: String
    )

    @GET("api/puntos")
    suspend fun getPuntos(): List<PuntoMarcado>

    @POST("api/puntos")
    suspend fun postPunto(@Body punto: PuntoMarcado): PuntoMarcado

    @PUT("api/puntos/{id}")
    suspend fun putPunto(@Path("id") id: Int, @Body punto: PuntoMarcado): PuntoMarcado

    @DELETE("api/puntos/{id}")
    suspend fun deletePunto(@Path("id") id: Int)


}
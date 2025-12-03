package mx.edu.utez.ciudadsecreta.data.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import mx.edu.utez.ciudadsecreta.data.model.LoginRequest

interface ApiService {

    @POST("api/usuarios/login")
    suspend fun loginUsuario(@Body credentials: LoginRequest): Response<Void>
}
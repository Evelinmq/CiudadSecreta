package mx.edu.utez.ciudadsecreta.data.retrofit

import mx.edu.utez.ciudadsecreta.data.model.LoginRequest
import mx.edu.utez.ciudadsecreta.data.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUsuario(@Body credentials: LoginRequest): Response<Void>
    @POST("api/auth/register")
    suspend fun registrarUsuario(@Body user: UsuarioRequest): Response<Void>
}
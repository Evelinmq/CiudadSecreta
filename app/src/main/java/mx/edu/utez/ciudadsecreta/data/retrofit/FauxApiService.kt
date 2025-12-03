package mx.edu.utez.ciudadsecreta.data.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface FauxApiService {

    @GET("api/get")
    suspend fun loginUsuario(): Response<Void>
}
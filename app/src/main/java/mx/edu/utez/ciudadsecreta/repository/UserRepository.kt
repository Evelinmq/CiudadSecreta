package mx.edu.utez.ciudadsecreta.repository

import mx.edu.utez.ciudadsecreta.data.model.LoginRequest
import mx.edu.utez.ciudadsecreta.data.model.UsuarioRequest
import mx.edu.utez.ciudadsecreta.data.retrofit.ApiService


class UserRepository(private val api: ApiService) {

    suspend fun login(nombreUsuario: String, contrasena: String): Result<Boolean> {
        return try {
            val request = LoginRequest(nombreUsuario, contrasena)
            val response = api.loginUsuario(request)

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
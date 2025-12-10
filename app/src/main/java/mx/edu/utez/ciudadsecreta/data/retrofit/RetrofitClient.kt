package mx.edu.utez.ciudadsecreta.data.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.110.22:5000/"
    private var token: String? = null

    fun setToken(t: String) {
        token = t
    }

    private val headerInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val builder = original.newBuilder()
        val ts = (System.currentTimeMillis() / 1000).toString()
        builder.addHeader("X-Timestamp", ts)

        token?.let {
            if (it.isNotBlank()) builder.addHeader("Authorization", "Bearer $it")
        }

        chain.proceed(builder.build())
    }


    private val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val puntoApi: PuntoApi by lazy { retrofit.create(PuntoApi::class.java) }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /*private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val puntoApi: PuntoApi by lazy {
        retrofit.create(PuntoApi::class.java)
    }

     */
}


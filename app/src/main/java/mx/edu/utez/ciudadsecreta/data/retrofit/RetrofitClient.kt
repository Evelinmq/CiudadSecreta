package mx.edu.utez.ciudadsecreta.data.retrofit

object RetrofitClient {

    private const val BASE_URL = "https://url/"

    val api: PuntoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PuntoApi::class.java)
    }
}

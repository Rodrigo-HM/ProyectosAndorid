package com.example.peliculas.network



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: ApiService by lazy { //con lazy hace que solo lo cree si se va a usar
        Retrofit.Builder()
            .baseUrl("http://192.168.1.119:8080/")
            .addConverterFactory(GsonConverterFactory.create()) //convierte el JSON en bojetos Kotlin
            .build()
            .create(ApiService::class.java)
    }
}


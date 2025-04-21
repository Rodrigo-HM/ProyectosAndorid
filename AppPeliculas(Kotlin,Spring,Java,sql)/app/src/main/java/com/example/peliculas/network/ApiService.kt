package com.example.peliculas.network

import com.example.peliculas.model.Movie

import retrofit2.http.GET

interface ApiService {  //se crea la interfaz con /movis para meterlas en la lista
    @GET("/movies")
    suspend fun getMovies(): List<Movie>
}




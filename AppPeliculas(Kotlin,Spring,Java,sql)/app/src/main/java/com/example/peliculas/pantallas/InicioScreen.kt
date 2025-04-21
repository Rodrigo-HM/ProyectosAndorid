package com.example.peliculas.pantallas

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

//esta clase no se ve, solo tiene la logica de que si ya inició sesion no lo vuelva a pedir

@Composable
fun InicioScreen(navController: NavController) {
    val context = LocalContext.current  //contecxto actual
    val prefs = context.getSharedPreferences("userprefs", Context.MODE_PRIVATE) //almacena datos del usuario

    LaunchedEffect(Unit) {  //se ejecuta solo una vez gracias a unit
        val savedUser = prefs.getString("user", "")  //coge el valor de prefs si hay alguno guardado
        val savedPass = prefs.getString("pass", "")

        if (!savedUser.isNullOrEmpty() && !savedPass.isNullOrEmpty()) { //verifica si está vacio
            navController.navigate("movies") {  //te lleva a movie si enciuentra usuario anterior
                popUpTo("inicio") { inclusive = true } //con pop hace que esta pantalla se elimine
            }
        } else {
            navController.navigate("login") { //te lleva a login
                popUpTo("inicio") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


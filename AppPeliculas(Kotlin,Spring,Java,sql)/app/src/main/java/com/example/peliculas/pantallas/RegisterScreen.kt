package com.example.peliculas.pantallas

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment


@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("userprefs", Context.MODE_PRIVATE) //accede a los datos de usuarios
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize() //Ocupa todo el espacio disponible
            .padding(16.dp),
        contentAlignment = Alignment.Center //Centra el contenido
    ) {
        Column(
            Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = user, onValueChange = { user = it }, label = { Text("Usuario") }) //se usa una lambda para meter el texto en it, asignandolo a user
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") })
            Spacer(modifier = Modifier.height(10.dp))

            //Mostrar mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(onClick = {
                if (user.isEmpty() || pass.isEmpty()) {
                    errorMessage = "Por favor, complete todos los campos"
                } else {
                    //Comprobar si el usuario ya existe
                    val savedUser = prefs.getString("user", "")
                    if (savedUser == user) {
                        errorMessage = "Este usuario ya está registrado"
                    } else {
                        //Guardar usuario y contraseña
                        prefs.edit().putString("user", user).putString("pass", pass).apply()
                        navController.navigate("login")
                    }
                }
            }) {
                Text("Registrarse")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    // Para poder visualizar el preview, necesitamos un NavController
    val navController = rememberNavController()
    RegisterScreen(navController)
}








package com.example.peliculas.pantallas


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.*
import androidx.compose.ui.Alignment


@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("userprefs", Context.MODE_PRIVATE)  //guarda los datos de usuarios que haya
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.padding(20.dp),horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(value = user, onValueChange = { user = it }, label = { Text("Usuario") }) //se usa una lambda para meter el texto en it, asignandolo a user
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") })
            Spacer(modifier = Modifier.height(10.dp))

            // Mostrar mensaje de error si existe
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(10.dp))
            }

            Button(onClick = {
                val savedUser = prefs.getString("user", "")
                val savedPass = prefs.getString("pass", "")
                if (user == savedUser && pass == savedPass) {
                    navController.navigate("movies")
                } else {
                    if (user.isEmpty() || pass.isEmpty()) {
                        errorMessage = "Por favor, complete todos los campos"
                    }else{
                    errorMessage = "Usuario o contraseña incorrecta"}
                }
            }) {
                Text("Iniciar sesión")
            }

            Spacer(modifier = Modifier.height(50.dp))  // Espacio entre los botones
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("¿Aún no te has registrado?", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.width(12.dp)) // Espacio entre el texto y el botón
                Button(onClick = {
                    navController.navigate("register")
                }) {
                    Text("Regístrate")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    // Para poder visualizar el preview, necesitamos un NavController
    val navController = rememberNavController()
    LoginScreen(navController)
}





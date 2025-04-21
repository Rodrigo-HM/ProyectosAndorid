package com.example.peliculas.pantallas

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.peliculas.model.Movie
import com.example.peliculas.network.RetrofitClient
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import java.net.URLEncoder

@Composable
fun MoviesScreen(navController: NavHostController) {
    val context = LocalContext.current
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }  //guarda la lista de peliculas de la apo
    var isLoading by remember { mutableStateOf(true) } //para ver si carga la info

    LaunchedEffect(Unit) { //lo ejecuta por primera vez
        try {
            movies = RetrofitClient.api.getMovies().sortedBy { it.movie_name }  //obtiene con retrofit y ordena alfabeticamente
        } catch (e: Exception) {
            Toast.makeText(context, "Error al cargar películas", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false //finaliza el estado de carga
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies) { movie ->
                    MovieCard(movie)
                }
            }
        }

        Box(  //boton para cambiar de usuario
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary) //Fondo del botón
                .clickable { navController.navigate("login") }, //Acción al pulsar
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Cambiar de usuario",
                tint = Color.White // Color del icono
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieCard(movie: Movie) {
    val context = LocalContext.current
    val imageName = "id_${movie.id}" //Para buscar la imagen segun id, poniendo antes id:

    val imageResId = remember(imageName) {
        context.resources.getIdentifier(imageName, "drawable", context.packageName) //carga la imagen desde carpeta drawable
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()

            .padding(8.dp),

        shape = RoundedCornerShape(12.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Imagen no encontrada", color = Color.Red)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = movie.movie_name,
                style = MaterialTheme.typography.headlineSmall.copy(Color.Black),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = movie.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Agregar un botón para ver más detalles
            Button(
                onClick = {
                    val query = URLEncoder.encode(movie.movie_name, "UTF-8")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query"))
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Ver más detalles")
            }
        }
    }

}



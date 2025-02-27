package com.example.botones

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.botones.ui.theme.BotonesTheme
import kotlin.random.Random
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay


data class Pez(val imagen: Int, val valor: Int) //para poner valor e imagen a los peces
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplica el tema de la app
            BotonesTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Llama a la función principal de la app
                    BuzoApp()
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun BuzoApp() {// Variables de estado
    var monedas by remember { mutableIntStateOf(5000) } //tendiran que ser 0 monedas al inicio, pero hay 5000 para poder probar todo
    var profundidad by remember { mutableIntStateOf(0) }
    //var inventarioPeces by remember { mutableStateOf(mutableListOf<Pez>()) }  con esta la UI no se actualiza, habría que reasiganrla
    val inventarioPeces = remember { mutableStateListOf<Pez>() } //esta detecta automaticamente los cambios en compose, pero con val no cambia la referencia
    var mostrarInventario by remember { mutableStateOf(false) }
    var posicionImagen by remember { mutableFloatStateOf(-320f) }
    var rotationAngle by remember { mutableFloatStateOf(270f) }
    var pezPescadoReciente by remember { mutableStateOf<Pez?>(null) } // con ? puede ser null
    var mostrarMensaje by remember { mutableStateOf(false) }

    var mejorasBuzo by remember { mutableIntStateOf(0) }
    var mejorasMochila by remember { mutableIntStateOf(1) }
    var tiendaAbierta by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") } //Estado para el mensaje de error

    //Función para obtener la profundidad máxima permitida según las mejoras compradas
    fun profundidadMaxima(): Int {
        return when (mejorasBuzo) {
            0 -> 1
            1 -> 2
            2 -> 3
            3 -> 4
            4 -> 5
            else -> 7 
        }
    }

    //Función para verificar si puede bajar
    fun puedeBajar(): Boolean {
        return profundidad < profundidadMaxima() && posicionImagen + 100f <= 200f
    }

    //Función para bajar el buzo
    fun bajarBuzo() {
        if (puedeBajar()) {
            posicionImagen += 100f
            rotationAngle = 360f
            profundidad++
        }
    }

    //Función para subir el buzo
    fun subirBuzo() {
        if (posicionImagen > -320f) {
            posicionImagen -= 100f
            rotationAngle = if (profundidad == 1) 270f else 180f
            if (profundidad > 0) profundidad--
        }
    }
    @Composable
    fun Tienda() {

        var precioMejoraBuzo by remember { mutableStateOf(100) } //Precio inicial buzo
        var precioMejoraMochila by remember { mutableStateOf(200) } //Precio inicial mochila

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .zIndex(9f), //para poner encima de todo
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                // Mejoras Buzo
                Text(text = "Mejoras buzo: $mejorasBuzo/5", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(20.dp))
                // Barra de progreso
                LinearProgressIndicator(
                    progress = { mejorasBuzo / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp)), //redondeado
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de compra
                Button(
                    onClick = {
                        if (mejorasBuzo < 5 && monedas>=precioMejoraBuzo){ //Suma hasta 4
                            mejorasBuzo++
                            monedas-=precioMejoraBuzo
                            precioMejoraBuzo+=100

                        }
                    }
                ) {
                    if(mejorasBuzo==5){ //cambia cuando llega al maximo de mejora
                        Text(text = "MAX")
                    }else{
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "$precioMejoraBuzo")
                            Spacer(modifier = Modifier.width(4.dp)) //Espacio entre el texto y la imagen
                            Image(
                                painter = painterResource(id = R.drawable.moneda),
                                contentDescription = "Moneda",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp)) //espaciado entre mejoras

                // Mejoras Mochila
                Text(text = "Mejoras mochila: ${mejorasMochila - 1}/3", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // Barra de progreso
                LinearProgressIndicator(
                    progress = { (mejorasMochila - 1) / 3f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de compra
                Button(
                    onClick = {
                        if (mejorasMochila < 4 && monedas>=precioMejoraMochila){ //Suma hasta 3
                            mejorasMochila++
                            monedas-=precioMejoraMochila
                            precioMejoraMochila+=100
                        }
                    }
                ) {
                    if(mejorasMochila==4){
                        Text(text = "MAX")
                    }else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically //Alinea los elementos en el centro
                        ) {
                            Text(text = "$precioMejoraMochila") //Precio de la mejora
                            Spacer(modifier = Modifier.width(4.dp)) //Espacio entre el texto y la imagen
                            Image(
                                painter = painterResource(id = R.drawable.moneda),
                                contentDescription = "Moneda",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.BottomCenter //Alinea el botón dentro de la imagen
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ganzua),
                        contentDescription = "Tesoro en la parte inferior",
                        modifier = Modifier.fillMaxSize()
                    )
                    Button(
                        onClick = {
                            if (monedas >= 1000&&inventarioPeces.size < (mejorasMochila*6)) {
                                monedas -= 1000
                                val ganzua =Pez(R.drawable.ganzua,1000)
                                inventarioPeces.add(ganzua)
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                    ) { Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "1000") // Número de monedas
                        Spacer(modifier = Modifier.width(4.dp)) //Espacio entre el número y la imagen
                        Image(
                            painter = painterResource(id = R.drawable.moneda),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    }
                }
                Spacer(modifier = Modifier.height(140.dp))
                //Botón de cerrar (abajo del todo)
                Image(
                    painter = painterResource(id = R.drawable.cerrar),
                    contentDescription = "Cerrar inventario",
                    modifier = Modifier
                        .padding(bottom = 0.dp)
                        .size(70.dp)
                        .clickable { tiendaAbierta = false } //Cierra la tienda al hacer clic
                )
            }
        }
    }
    //Funcion abrir cofre
    @Composable
    fun abrir(): Boolean {
        var abrirCofre by remember { mutableStateOf(false) } //Estado para saber si se ha abierto

        if (profundidad == 5 && inventarioPeces.any { it.imagen == R.drawable.ganzua }) { //para ver si teien ganzua en inventario
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { abrirCofre = true }, //Cambia a true cuando se pulsa, es una funcion lambda
                    modifier = Modifier.size(150.dp)
                ) {
                    Text(text = "Abrir", fontSize = 24.sp)
                }
            }
        }

        return abrirCofre //Devuelve true si se ha abierto el cofre
    }

    //Funcion vender
    fun vender() {
        if(profundidad==0) {
            val totalGanado = inventarioPeces.sumOf { it.valor } //Suma los valores de todos los peces
            monedas += totalGanado //Suma total ganado a monedas
            inventarioPeces.clear() //Vacía la lista
            mensajeError="" //lo vacia

        }else{
            mensajeError="Necesitas subir a la superficie"
        }
    }


    //Función para pescar
    fun pescar() {
        if (profundidad == 0) {
            return
        }

        val probabilidad = Random.nextInt(0, 100)
        val pezPescado = when (profundidad) {
            1 -> when {
                probabilidad < 10 -> Pez(R.drawable.atun, 10)
                probabilidad < 30 -> Pez(R.drawable.sardina, 5)
                else -> null
            }
            2 -> when {
                probabilidad < 10 -> Pez(R.drawable.rodaballo, 15)
                probabilidad < 30 -> Pez(R.drawable.atun, 10)
                else -> null
            }
            3 -> when {
                probabilidad < 10 -> Pez(R.drawable.merluza, 25)
                probabilidad < 30 -> Pez(R.drawable.rodaballo, 15)
                else -> null
            }
            4 -> when {
                probabilidad < 10 -> Pez(R.drawable.caviar, 50)
                probabilidad < 30 -> Pez(R.drawable.merluza, 25)
                else -> null
            }
            5 -> when {
                probabilidad < 10 -> Pez(R.drawable.perla, 100)
                probabilidad < 30 -> Pez(R.drawable.caviar, 50)
                else -> null
            }
            else -> null
        }

        if (pezPescado != null && inventarioPeces.size < (mejorasMochila*6)) {
            inventarioPeces.add(pezPescado)

            // Muestra el mensaje
            pezPescadoReciente = pezPescado
            mostrarMensaje = true //debajo despues de un segundo se pasa a false para que se quite

        }


    }

    Box(modifier = Modifier.fillMaxSize()) {

        //Fondo semitransparente si la tienda o inventario están abiertos
        if (tiendaAbierta || mostrarInventario) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .zIndex(5f) //encima de todo
            )
        }
        //Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo3),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop, //hace que ocupe toda la pantalla sin cambiar de tamaño
            modifier = Modifier.fillMaxSize()
        )

        //Buzo
        Column(
            modifier = Modifier.fillMaxSize().zIndex(1f), //por encima del fondo
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            val imagenBuzo = when (mejorasBuzo) { //cambiar buzo segun la mejora
                1 -> R.drawable.buzo2 
                2 -> R.drawable.buzo3
                3 -> R.drawable.buzo4
                4 -> R.drawable.buzo5
                5 -> R.drawable.buzo6
                else -> R.drawable.buzo1 //imagen inicio, cuando mejora es 0
            }

            Image(
                painter = painterResource(id = imagenBuzo),
                contentDescription = "Buzo",
                modifier = Modifier
                    .offset(y = posicionImagen.dp)
                    .graphicsLayer(rotationZ = rotationAngle) //se rotanen funcion de si baja o sube
            )
        }
        //Tesoro
        Image(
            painter = painterResource(id = R.drawable.tesoro),
            contentDescription = "Tesoro en la parte inferior",
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.BottomCenter) //En la parte inferior
                .padding(bottom = 16.dp)
                .zIndex(0f) //por debajo del buzo

        )
        //Muestra la ganancia en la parte superior izquierda
        Row( //row para poder poner texto y leugo imagen
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically //Alinea los elementos verticalmente
        ) {
            Text(
                text = "$monedas", //Número de monedas

            )
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el texto y la imagen
            Image(
                painter = painterResource(id = R.drawable.moneda), // Reemplaza con tu imagen de moneda
                contentDescription = "Moneda",
                modifier = Modifier.size(24.dp) // Ajusta el tamaño de la imagen
            )
        }
        Text(
            text = " ${profundidad*100} m",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )

        // Controles
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { subirBuzo() }) {
                Image(
                    painter = painterResource(id = R.drawable.arriba),
                    contentDescription = "Subir",
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { bajarBuzo() }) {
                Image(
                    painter = painterResource(id = R.drawable.abajo),
                    contentDescription = "Bajar",
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { pescar() }) {
                Image(
                    painter = painterResource(id = R.drawable.red),
                    contentDescription = "Pescar",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { mostrarInventario = !mostrarInventario },
                modifier = Modifier.size(90.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.mochila),
                        contentDescription = "Botón  inventario",
                        modifier = Modifier.size(50.dp) // Ajustar tamaño de la imagen
                    )

                    //Contador de peces en la esquina superior derecha
                    if (inventarioPeces.size >= mejorasMochila * 6) {
                        Text(
                            text = "❗",
                            fontSize = 17.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)

                        )
                    } else {
                        Text(
                            text = "x${inventarioPeces.size}",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {tiendaAbierta = !tiendaAbierta},
                modifier = Modifier.size(90.dp) 

            ) {
                Image(
                    painter = painterResource(id = R.drawable.tienda),
                    contentDescription = "Botón tienda",
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        //Inventario
        if (mostrarInventario&&!tiendaAbierta) { //asi no deja abrir inventario si la tienda esta abierta
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .zIndex(10f), //encima de todo
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.corcho),
                    contentDescription = "Inventario",
                    modifier = Modifier.fillMaxWidth().size(300.dp)
                )

                LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(16.dp)) { //tres columnas ordenadas
                    items(inventarioPeces) { pez -> //para cada pez se crea un item
                        Image(
                            painter = painterResource(id = pez.imagen), //para acceder a la imagen del pez
                            contentDescription = "Pez pescado",
                            modifier = Modifier.size((80/mejorasMochila).dp) //para que sea proporcional cuando aumente el espacio
                        )
                    }
                }


                Button(onClick = { vender() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 236.dp)
                    ) {
                    val totalGanado = inventarioPeces.sumOf { it.valor } //Calcula el total antes de vender
                    Row(verticalAlignment = Alignment.CenterVertically) { //en row para poner dinero e imagen
                        Text(text = "Vender $totalGanado ")
                        Image(
                            painter = painterResource(id = R.drawable.moneda),
                            contentDescription = "Moneda",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                //Botón de cerrar
                Image(
                    painter = painterResource(id = R.drawable.cerrar),
                    contentDescription = "Cerrar inventario",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp)
                        .size(70.dp)
                        .clickable { mostrarInventario = false } // Cierra el inventario al hacer clic
                )

            }
        }
        // Mostrar la tienda cuando mostrarTienda es true y no esta abierto el inventario
        if (tiendaAbierta&&!mostrarInventario) {
            Tienda()
        }

        //Muestra el mensaje de error cuando este se aplica
        if (mensajeError.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)) //fondo oscurecido
                    .zIndex(20f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mensajeError,
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Red, shape = RoundedCornerShape(8.dp)) //rojo con bordes redondeados
                        .padding(16.dp)
                )
            }
        }
        //Borra el mensaje después de un segundo
        LaunchedEffect(mensajeError) {
            if (mensajeError.isNotEmpty()) {
                delay(1000)
                mensajeError = ""
            }
        }
        if (abrir()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .zIndex(20f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp)) //redondea bordes
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mono),
                        contentDescription = "Mono",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Final del juego",
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Todos los clicks y el tiempo perdido para absolutamente nada :)",
                        fontSize = 14.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            

        }

        //para mostrar peces cada vez que se pescan
        if (mostrarMensaje && pezPescadoReciente != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(15f),
                contentAlignment = Alignment.Center
            ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = pezPescadoReciente!!.imagen),//te obliga a poner !! afirmando que no pueda ser null
                            contentDescription = "Pez pescado",
                            modifier = Modifier.size(94.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "x1", fontSize = 30.sp)
                    }

            }
            // Oculta el mensaje después de 1 segundo
            LaunchedEffect(Unit) {
                delay(1000)
                mostrarMensaje = false
            }
        }




    }
}

@Preview(showBackground = true)
@Composable
fun BuzoPreview() {
    // Permite visualizar el diseño en Android Studio
    BotonesTheme {
        BuzoApp()
    }
}


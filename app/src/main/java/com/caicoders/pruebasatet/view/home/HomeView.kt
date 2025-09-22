package com.caicoders.pruebasatet.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.caicoders.pruebasatet.R
import com.caicoders.pruebasatet.ui.theme.Purpura
import com.caicoders.pruebasatet.view.login.montserrat

@Composable
fun HomeScreen(navController: NavHostController) {
    Login(navController)
}


@Composable
fun Login(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.difuminado),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Cuadro(navController, R.drawable.webcam, "Entrenar", "Training", false)
                Cuadro(navController, R.drawable.eye, "Evaluación", "Training", true)
                Cuadro(navController, R.drawable.doc, "Informes", "Report", true)
            }
        }
    }
}

@Composable
fun Cuadro(
    navController: NavHostController,
    element: Int,
    title: String,
    route: String,
    isEnabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .height(180.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(if (isEnabled) Purpura else Color.Gray)
                .clickable(enabled = isEnabled) { if (isEnabled) navController.navigate(route) },
            Alignment.Center
        ) {
            Image(
                painterResource(id = element),
                contentDescription = "Calibration",
                colorFilter = if (isEnabled) null else ColorFilter.colorMatrix(ColorMatrix().apply {
                    setToSaturation(0f)
                }))
        }
        Text(
            text = title,
            fontFamily = montserrat,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
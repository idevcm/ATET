package com.caicoders.pruebasatet.view.training

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.caicoders.pruebasatet.R
import com.caicoders.pruebasatet.ui.theme.Purpura

@Composable
fun TrainingScreen(navController: NavController) {
    SelectStage(navController)
}

@Composable
fun SelectStage(navController: NavController) {
    val days = Array(4) { row -> Array(7) { col -> "Día ${row * 7 + col + 1}" } }

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

        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            val boxSize = minOf(maxWidth, maxHeight) / 5.5f

            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                days.forEach { week ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        week.forEachIndexed { index, day ->
                            Squadron(day, navController, boxSize, day == "Día 1")
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun Squadron(day: String, navController: NavController, boxSize: Dp, isEnabled: Boolean) {
    val color = if (isEnabled) Purpura else Color.Gray
    val clickableModifier =
        if (isEnabled) Modifier.clickable { navController.navigate("Calibration") } else Modifier

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.size(boxSize) // Tamaño calculado
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Llena el tamaño del Column
                .clip(RoundedCornerShape(10.dp))
                .background(color)
                .then(clickableModifier),
            Alignment.Center
        ) {
            Text(
                text = day,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}
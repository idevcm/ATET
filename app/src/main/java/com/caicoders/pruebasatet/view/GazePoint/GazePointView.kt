package com.caicoders.pruebasatet.view.GazePoint

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.caicoders.pruebasatet.ui.theme.Marron

@Composable
fun GazePointView(
    x: Float,
    y: Float,
    remonstrantCalibration: Boolean,
    isCalibrationFinished: Boolean
) {
    val circleSize = 15f
    Box() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val safeX = if (x.isNaN()) 0f else x
            val safeY = if (y.isNaN()) 0f else y

            if (!remonstrantCalibration && (!safeX.isNaN() && !safeY.isNaN()) && isCalibrationFinished) {
                drawCircle(
                    color = Marron,
                    center = Offset(x = safeX, y = safeY - 100.dp.toPx()),
                    radius = circleSize
                )
            }
        }
    }
}
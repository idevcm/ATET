package com.caicoders.pruebasatet.view.stages

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.caicoders.pruebasatet.R
import com.caicoders.pruebasatet.utils.Tracker.TrackerModel
import com.caicoders.pruebasatet.utils.arrayOfColors
import com.caicoders.pruebasatet.view.GazePoint.GazePointView
import com.caicoders.pruebasatet.view.ballon.BallonModel
import com.caicoders.pruebasatet.view.ballon.BalloonImage

@Composable
fun BallonScreen(trackerModel: TrackerModel, ballonModel: BallonModel) {

    var nextColor by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize().background(arrayOfColors()[nextColor])
    ) {
        val isBallonVisible = ballonModel.isBalloonVisible.collectAsState()
        val collisionPoint = ballonModel.collisionPoint.collectAsState()
        val isCalibrationFinished = trackerModel.isCalibrationFinished.collectAsState()
        var x by remember { mutableStateOf(100f) }
        var y by remember { mutableStateOf(100f) }
        var balloonCount by remember { mutableStateOf(0)}
        val mostrarPuntoCalibracion = remember { mutableStateOf(false) }
        val iniciarCalibracion = remember { mutableStateOf(false) }
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val screenHeight = configuration.screenHeightDp

        var randomX by remember {
            mutableStateOf<Float>(
                ((screenWidth * 0.2).toInt() until screenWidth - (screenWidth * 0.2).toInt()).random()
                    .toFloat()
            )
        }
        var randomY by remember {
            mutableStateOf<Float>(
                ((screenHeight * 0.2).toInt() until screenHeight - (screenHeight * 0.2).toInt()).random()
                    .toFloat()
            )
        }

        LaunchedEffect(trackerModel) {
            trackerModel.xx.collect { value ->
                x = value
            }
        }

        LaunchedEffect(trackerModel) {
            trackerModel.yy.collect { value ->
                y = value
            }
        }

//        Image(
//            painterResource(id = R.drawable.eye),
//            contentDescription = "Fondo",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds
//        )

        Column {
            if (isBallonVisible.value) {
                BalloonImage(collisionPoint.value)
            }

            GazePointView(
                x,
                y,
                mostrarPuntoCalibracion.value,
                isCalibrationFinished.value
            )

            if (isCalibrationFinished.value) {
                mostrarPuntoCalibracion.value = false
                iniciarCalibracion.value = false
            }

            LaunchedEffect(x, y) {
                val marginX = (screenWidth * 0.2).toInt()
                val marginY = (screenHeight * 0.2).toInt()

                randomX = (marginX until screenWidth - marginX).random().toFloat()
                randomY = (marginY until screenHeight - marginY).random().toFloat()

                if (!ballonModel.isBalloonVisible.value && balloonCount < 50 && isCalibrationFinished.value) {
                    balloonCount++
                    ballonModel.setBalloonPosition(Offset(randomX, randomY), Size(200f, 200f))
                    ballonModel._isBalloonVisible.value = true
                    if (balloonCount % 10 == 0 && balloonCount < 50){
                        nextColor++
                    }
                }
                if (x.isFinite() && y.isFinite()) {
                    ballonModel.checkCollision(
                        pointPosition = Offset(x, y),
                        balloonSize = Size(
                            100f,
                            200f
                        ),
                        screenHeight = screenHeight.toFloat()
                    )
                }
            }
        }
    }
}

@Composable
fun loadBitmap(resId: Int, sampleSize: Int): ImageBitmap {
    val options = BitmapFactory.Options().apply {
        inSampleSize = sampleSize
    }

    val bitmap = BitmapFactory.decodeResource(LocalContext.current.resources, resId, options)
    return bitmap.asImageBitmap()
}
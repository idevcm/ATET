package com.caicoders.pruebasatet.view.calibration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.caicoders.pruebasatet.utils.Tracker.TrackerModel
import com.caicoders.pruebasatet.view.ballon.BallonModel
import com.caicoders.pruebasatet.view.stages.BallonScreen

@Composable
fun StartGame(trackerModel: TrackerModel, ballonModel: BallonModel, calibrationModel: CalibrationModel) {
    val iniciarPrograma = remember { mutableStateOf(true) }
    val iniciarTrackeo = remember { mutableStateOf(false) }
    val iniciarCalibracion = remember { mutableStateOf(false) }
    val mostrarPuntoCalibracion = remember { mutableStateOf(false) }
    var calibrationButtonPosition by remember { mutableStateOf(Offset.Zero) }
    val showCruz = calibrationModel.showCruz.collectAsState()


    val calibrationX = trackerModel.calibrationIconX.collectAsState()
    val calibrationY = trackerModel.calibrationIconY.collectAsState()
    val isCalibrationFinished = trackerModel.isCalibrationFinished.collectAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!showCruz.value) {
            Button(onClick = {
                iniciarTrackeo.value = true
            }) {
                Text("Iniciar Juego")
            }

            Button(onClick = {
                calibrationModel.setShowCruz(true)
                iniciarCalibracion.value = true
                mostrarPuntoCalibracion.value = true
                calibrationButtonPosition = Offset(x = calibrationX.value, y = calibrationY.value)
            }) {
                Text(text = "Calibrar")
            }
        }

        if (showCruz.value) {
            Text(
                text = "+",
                fontSize = 100.sp,
                color = Color.Black
            )
        }
    }

    if (isCalibrationFinished.value) {
        BallonScreen(trackerModel, ballonModel)
    }

    if (iniciarCalibracion.value) {
        CalibrationThread(trackerModel)
        iniciarCalibracion.value = false
    }

    if (iniciarPrograma.value) {
        TrackerModelThread(trackerModel)
    }

    if (iniciarTrackeo.value) {
        TrackingGazeThread(trackerModel)
    }
}

@Composable
fun TrackerModelThread(loginModel: TrackerModel) {
    LaunchedEffect(Unit) {
        loginModel.initGazeTracker()
    }
}

@Composable
fun TrackingGazeThread(loginModel: TrackerModel) {
    LaunchedEffect(Unit) {
        loginModel.startTrackingGaze()
    }
}

@Composable
fun CalibrationThread(trackerModel: TrackerModel) {
    LaunchedEffect(Unit) {
        trackerModel.calibrate()
    }
}
package com.caicoders.pruebasatet.utils.Tracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun Why(navController: NavController) {
    val context = LocalContext.current
    val trackerModel = TrackerModel()
    trackerModel.instanciarGazeTracker(context)
    trackerModel.requestCameraPermission(context as Activity)
    trackerModel.setGazeCallbacks()

    TestComposable(navController, trackerModel, context)
}


@Composable
fun TestComposable(
    navController: NavController,
    trackerModel: TrackerModel,
    context: Context
) {

    val iniciarPrograma = remember { mutableStateOf(false) }
    val iniciarTrackeo = remember { mutableStateOf(false) }
    val iniciarCalibracion = remember { mutableStateOf(false) }
    val mostrarPuntoCalibracion = remember { mutableStateOf(false) }

    val isCalibrating = trackerModel.isCalibrating.collectAsState()

    val cameraPermissionStatus = trackerModel.cameraPermissionStatus.collectAsState()

    // Recoge las coordenadas de la mirada
    val x = trackerModel.xx.collectAsState()
    val y = trackerModel.yy.collectAsState()

    //Calibracion
    val calibrationX = trackerModel.calibrationIconX.collectAsState()
    val calibrationY = trackerModel.calibrationIconY.collectAsState()
    val isCalibrationFinished = trackerModel.isCalibrationFinished.collectAsState()


    Column {
        // Añade el botón de iniciar el seguimiento de la mirada
        Button(onClick = {
            iniciarPrograma.value = true
        }) {
            Text("Iniciar GazeTracker")
        }

        Button(onClick = {
            iniciarTrackeo.value = true
        }) {
            Text("Iniciar seguimiento de la mirada")
        }

        Button(onClick = {
            iniciarCalibracion.value = true
            mostrarPuntoCalibracion.value = true
        }) {
            Text(text = "Calibrar")
        }

        Text("Estado del permiso de la cámara: ${cameraPermissionStatus.value}")

        GazePointView(x.value, y.value, calibrationX, calibrationY, mostrarPuntoCalibracion.value)
    }

    if (iniciarPrograma.value) {
        hilo(trackerModel)
    }

    if (iniciarTrackeo.value) {
        hilo2(trackerModel)
    }

    if (iniciarCalibracion.value) {
        hilo3(trackerModel)
    }

    if(isCalibrationFinished.value){
        mostrarPuntoCalibracion.value = false
        iniciarCalibracion.value = false
    }


// Asegúrate de reemplazar `context` con tu contexto actual
    val permission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

    if (permission == PackageManager.PERMISSION_GRANTED) {
        Log.d("CameraPermission", "La aplicación tiene permiso para usar la cámara.")
    } else {
        Log.d("CameraPermission", "La aplicación no tiene permiso para usar la cámara.")
    }


}

@Composable
fun GazePointView(
    x: Float,
    y: Float,
    calibrationX: State<Float>,
    calibrationY: State<Float>,
    remonstrantCalibration: Boolean
) {
    val circleSize = 25f
    Box() {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val safeX = x ?: 0f
            val safeY = y ?: 0f

            if (!remonstrantCalibration && (!safeX.isNaN() && !safeY.isNaN())) {
                drawCircle(color = Color.Red, center = Offset(x = safeX, y = safeY), radius = circleSize)
            }else{
                drawCircle(color = Color.Blue, center = Offset(x = (calibrationX.value) , y = (calibrationY.value / 2) + 80) , radius = circleSize)
            }
        }
    }
}

@Composable
fun hilo(loginModel: TrackerModel) {
    LaunchedEffect(Unit) {
        loginModel.initGazeTracker()
    }
}

@Composable
fun hilo2(loginModel: TrackerModel) {
    LaunchedEffect(Unit) {
        loginModel.startTrackingGaze()
    }
}

@Composable
fun hilo3(trackerModel: TrackerModel) {
    LaunchedEffect(Unit) {
        trackerModel.calibrate()
    }
}

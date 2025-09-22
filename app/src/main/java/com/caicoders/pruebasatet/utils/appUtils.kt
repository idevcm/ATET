package com.caicoders.pruebasatet.utils

import android.view.OrientationEventListener
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.caicoders.pruebasatet.ui.theme.*

@Composable
fun CheckOrientation() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val orientationEventListener = object : OrientationEventListener(context) {
        override fun onOrientationChanged(orientation: Int) {
            showDialog = orientation in 0..45 || orientation in 315..359
        }
    }

    orientationEventListener.enable()


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Alerta") },
            text = { Text("Por favor, cambie a orientación horizontal.") },
            confirmButton = {
                TextButton(onClick = { }) {
                    Text("")
                }
            }
        )
    }
}

fun arrayOfColors(): List<Color> {
    return listOf(
        Yellow,
        Verde,
        Azul,
        Naranja,
        Rojo
    )
}
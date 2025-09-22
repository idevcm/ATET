package com.caicoders.pruebasatet

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.caicoders.pruebasatet.nav.NavigationGraft
import com.caicoders.pruebasatet.ui.theme.PruebasATETTheme
import com.caicoders.pruebasatet.utils.Tracker.TrackerModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebasATETTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val trackerModel = TrackerModel()
                    trackerModel.instanciarGazeTracker(context)
                    trackerModel.requestCameraPermission(context as Activity)
                    trackerModel.setGazeCallbacks()

                    NavigationGraft(context, trackerModel)
                }
            }
        }
    }
}

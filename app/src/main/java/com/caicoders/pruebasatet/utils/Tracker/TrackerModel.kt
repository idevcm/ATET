package com.caicoders.pruebasatet.utils.Tracker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import camp.visual.android.kotlin.sample.manager.GazeTrackerManager
import camp.visual.gazetracker.callback.CalibrationCallback
import camp.visual.gazetracker.callback.GazeCallback
import camp.visual.gazetracker.callback.InitializationCallback
import camp.visual.gazetracker.callback.StatusCallback
import camp.visual.gazetracker.callback.UserStatusCallback
import camp.visual.gazetracker.constant.AccuracyCriteria
import camp.visual.gazetracker.constant.CalibrationModeType
import camp.visual.gazetracker.constant.StatusErrorType
import camp.visual.gazetracker.gaze.GazeInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrackerModel : ViewModel() {

    // SeeSo
    private var _gazeTrackerManager: GazeTrackerManager? = null

    fun getGazeTrackerManager(): GazeTrackerManager? {
        return _gazeTrackerManager
    }

    var isGazeTrackingStarting = false
    var gazeTrackerFPS: Int = 30

    // Coordenadas de la mirada
    private val _x = MutableStateFlow(0f)
    val xx = _x.asStateFlow()

    private val _y = MutableStateFlow(0f)
    val yy = _y.asStateFlow()

    //Calibracion

    val _isCalibrating = MutableStateFlow(_gazeTrackerManager?.isCalibrating())
    val isCalibrating = _isCalibrating.asStateFlow()

    private val _calibrationProgress = MutableStateFlow(0f)
    val calibrationProgress = _calibrationProgress.asStateFlow()

    private val _calibrationIconX = MutableStateFlow(960f)
    val calibrationIconX = _calibrationIconX.asStateFlow()

    private val _calibrationIconY = MutableStateFlow(500f)
    val calibrationIconY = _calibrationIconY.asStateFlow()

    private val _isCalibrationFinished = MutableStateFlow(false)
    val isCalibrationFinished = _isCalibrationFinished.asStateFlow()

    // Atención
    private val _atencionScore = MutableStateFlow(0)
    val atencionScore = _atencionScore.asStateFlow()

    // Pestañeo
    private val _blinkVar = MutableStateFlow(false)
    val blinkVar = _blinkVar.asStateFlow()

    // Detección de sueño
    private val _isSleepy = MutableStateFlow(false)
    val isSleep = _isSleepy.asStateFlow()

    fun instanciarGazeTracker(context: Context): GazeTrackerManager? {
        _gazeTrackerManager = GazeTrackerManager.makeNewInstance(context)
        Log.d(
            "PutoGaze",
            "El puto gaze es ${_gazeTrackerManager?.getGazeTrackerState()} en instanciarGazeTracker"
        )
        return _gazeTrackerManager
    }


    // Funciones
    fun setGazeCallbacks() {
        Log.d(
            "PutoGaze",
            "El puto gaze es ${_gazeTrackerManager} en setGazeCallbacks"
        )
        _gazeTrackerManager?.setGazeTrackerCallbacks(
            gazeCallback,
            calibrationCallback,
            statusCallback,
            userStatusCallback,
        )
    }

    // 1.- Primer callback
    private val gazeCallback = object : GazeCallback {
        override fun onGaze(gazeInfo: GazeInfo) {
            // Log the gaze point coordinates
            Log.d("GazePoint", "X: ${gazeInfo.x}, Y: ${gazeInfo.y}")

            if (_gazeTrackerManager?.isCalibrating() == false) {
                CoroutineScope(Dispatchers.Main).launch {
                    _x.emit(gazeInfo.x)
                    _y.emit(gazeInfo.y)
                }
            }
        }
    }

    // 2.- Segundo callback
    private val calibrationCallback = object : CalibrationCallback {
        override fun onCalibrationProgress(progress: Float) {
            val percentage = (progress * 100)
            CoroutineScope(Dispatchers.Main).launch {
                _calibrationProgress.emit(percentage)
            }
        }

        override fun onCalibrationNextPoint(fx: Float, fy: Float) {
            CoroutineScope(Dispatchers.Main).launch {
                _calibrationIconX.emit(fx)
                _calibrationIconY.emit(fy)
            }

            CoroutineScope(Dispatchers.Main).launch {
                delay(1000)
                startCollectSamples()
            }
        }

        override fun onCalibrationFinished(calibrationData: DoubleArray?) {
            CoroutineScope(Dispatchers.Main).launch {
                _isCalibrationFinished.emit(true)
            }
        }
    }

    private fun startCollectSamples() {
        _gazeTrackerManager?.startCollectionCalibrationSamples()
    }

    // 3.- Tercer callback
    private val statusCallback = object : StatusCallback {
        override fun onStarted() {
            // will be called after gaze tracking started
        }

        override fun onStopped(error: StatusErrorType?) {
            // Check https://docs.seeso.io/docs/api/android-api-docs#statuserrortype
            when (error) {
                StatusErrorType.ERROR_NONE -> {}
                StatusErrorType.ERROR_CAMERA_START -> {}
                StatusErrorType.ERROR_CAMERA_INTERRUPT -> {}
                else -> {}
            }
        }
    }

    // 4.- Cuarto callback
    private val userStatusCallback = object : UserStatusCallback {
        override fun onAttention(timestampBegin: Long, timestampEnd: Long, score: Float) {
            CoroutineScope(Dispatchers.Main).launch {
                _atencionScore.emit((score * 100).toInt())
            }
        }

        override fun onBlink(
            timestamp: Long,
            isBlinkLeft: Boolean,
            isBlinkRight: Boolean,
            isBlink: Boolean,
            leftOpenness: Float,
            rightOpenness: Float
        ) {
            if (isBlink) {
                CoroutineScope(Dispatchers.Main).launch {
                    _blinkVar.emit(true)
                    delay(400)
                    _blinkVar.emit(false)
                }
            }
        }

        override fun onDrowsiness(timestamp: Long, isDrowsiness: Boolean, intensity: Float) {
            CoroutineScope(Dispatchers.Main).launch {
                _isSleepy.emit(isDrowsiness)
            }
        }
    }

    // 5.- Iniciar permisos de la camara
    val _cameraPermissionStatus = MutableStateFlow("Waiting for permission request.")
    val cameraPermissionStatus = _cameraPermissionStatus.asStateFlow()

    // Actualiza el estado en el método requestCameraPermission
    private val permissions = arrayOf(
        Manifest.permission.CAMERA
    )
    private val permissionCode: Int = 1000

    fun requestCameraPermission(activity: Activity) {
        when {
            ContextCompat.checkSelfPermission(
                activity,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED -> {
                _cameraPermissionStatus.value = "Camera permission is granted permanently."
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permissions[0]
            ) -> {
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    permissionCode
                )
                _cameraPermissionStatus.value = "Camera permission is granted temporarily."
            }

            else -> {
                ActivityCompat.requestPermissions(
                    activity,
                    permissions,
                    permissionCode
                )
                _cameraPermissionStatus.value = "Camera permission is denied."
            }
        }
    }


    // 5.- Iniciar el seguimiento de la mirada
    // Añade un MutableStateFlow para manejar el estado de la opción
    private val _withOption = MutableStateFlow(false)
    val withOption = _withOption.asStateFlow()

    // Define the MutableStateFlow
    private val _gazePosition = MutableStateFlow(Offset(40f, 40f)) // Initial position
    val gazePosition: StateFlow<Offset> = _gazePosition

    // Function to update the position
    fun updatePosition(newPosition: Offset) {
        _gazePosition.value = newPosition
    }


    fun initGazeTracker() {
        _gazeTrackerManager?.initGazeTracker(initializationCallback, false)

        // You can also set FPS of gazeTracker if you want
//        _gazeTrackerManager!!.setGazeTrackingFps(gazeTrackerFPS)
    }

    private val initializationCallback =
        InitializationCallback { gazeTracker, error ->
            if (gazeTracker != null) {
                CoroutineScope(Dispatchers.Main).launch {

                    Log.d(
                        "PutoGaze",
                        "Paso por initializationCallback, siendo gazetracker value = ${gazeTracker}"
                    )
                }
            }
        }

    fun emitGazeInfoX(gazeInfo: GazeInfo) {
        CoroutineScope(Dispatchers.Main).launch {
            _x.emit(gazeInfo.x)
            _y.emit(gazeInfo.y)
        }
    }

    fun startTrackingGaze() {
        Log.d(
            "PutoGaze",
            "El puto gaze en start TrackingGaze es ${_gazeTrackerManager?.getGazeTrackerState()}"
        )
        if (_gazeTrackerManager?.isTracking() != true) {
            startTracking()
        } else {
            stopTracking()
        }
    }

    private fun startTracking() {

        if (_gazeTrackerManager != null) {
            _gazeTrackerManager!!.startGazeTracking()
            Log.d(
                "PutoGaze",
                "El puto gaze es ${_gazeTrackerManager!!.getGazeTrackerState()} en startTracking"
            )

            Log.d(
                "PutoGaze",
                if (_gazeTrackerManager!!.isTracking()) "El puto gaze está trackeando" else "El puto gaze no está trackeando"
            )

        }

        isGazeTrackingStarting = true
    }

    private fun stopTracking() {
        _gazeTrackerManager?.stopGazeTracking()
    }

    fun calibrate() {
        _gazeTrackerManager?.startCalibration(CalibrationModeType.ONE_POINT, AccuracyCriteria.HIGH)
    }
}
package com.caicoders.pruebasatet.view.calibration

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalibrationModel {
    val _showCruz = MutableStateFlow(false)
    val showCruz = _showCruz.asStateFlow()

    fun setShowCruz(value: Boolean) {
        _showCruz.value = value
    }

}
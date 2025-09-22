package com.caicoders.pruebasatet.view.ballon

import android.graphics.Rect
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BallonModel : ViewModel() {

    val _isBalloonVisible = MutableStateFlow(true)
    val isBalloonVisible = _isBalloonVisible.asStateFlow()

    public fun setIsBalloonVisible(value: Boolean) {
        _isBalloonVisible.value = value
    }

    val _collisionPoint = MutableStateFlow(Offset(200f, 200f))
    val collisionPoint = _collisionPoint.asStateFlow()

    fun setBalloonPosition(balloonPosition: Offset, balloonSize: Size) {
        _collisionPoint.value = Offset(
            x = balloonPosition.x + balloonSize.width / 2,
            y = balloonPosition.y + balloonSize.height / 2
        )
    }

    fun checkCollision(
        pointPosition: Offset,
        balloonSize: Size,
        screenHeight: Float
    ) {
        val collisionRectTopHalf = Rect(
            (collisionPoint.value.x - balloonSize.width / 2).toInt(),
            (collisionPoint.value.y - balloonSize.height).toInt(),
            (collisionPoint.value.x + balloonSize.width / 2).toInt(),
            (collisionPoint.value.y).toInt()
        )

        if (collisionRectTopHalf.contains(pointPosition.x.toInt(), pointPosition.y.toInt())) {
            Log.d("TAG", "SE HIZO COLISION")
            _isBalloonVisible.value = false
        }
    }
}
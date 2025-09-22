package com.caicoders.pruebasatet.view.ballon

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.caicoders.pruebasatet.R

@Composable
fun BalloonImage(collisionPoint: Offset) {
    val imageSize = 100.dp
    val halfImageSize = imageSize / 2

    Box(modifier = Modifier
        .offset {
            IntOffset(
                (collisionPoint.x - halfImageSize.toPx()).toInt(),
                (collisionPoint.y - halfImageSize.toPx()).toInt()
            )
        }
        .size(imageSize, imageSize), contentAlignment = Alignment.Center) {
        Image(
            painterResource(id = R.drawable.globooso), contentDescription = "globo"
        )
    }
}
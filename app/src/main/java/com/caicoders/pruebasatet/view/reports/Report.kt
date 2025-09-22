package com.caicoders.pruebasatet.view.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.caicoders.pruebasatet.R
import com.caicoders.pruebasatet.utils.arrayOfColors

@Preview(showBackground = true, showSystemUi = true, device = Devices.TABLET)
@Composable
fun ReportPreview() {
    val colors = arrayOfColors()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Reporte de Globos", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Número de globos destruidos: 50")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tiempo: 1:22 minutos")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Color alcanzado: Morado")
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow {
            items(colors) { color ->
                Box(modifier = Modifier.size(50.dp).padding(4.dp).background(color = color).clip(MaterialTheme.shapes.small))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = R.drawable.globooso), contentDescription = "Imagen de globo")
    }
}
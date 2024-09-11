package com.joel.home.contents

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joel.home.R

data class GridItem(
    val type: GridItemType,
    val data: Any? = null // Use this to pass specific data for each type
)

enum class GridItemType {
    Temperature,
    UVIndex,
    Humidity,
    Wind,
    Precipitation,
    Pressure
}

@Composable
fun WeatherGridItems(items: List<GridItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            when (item.type) {
                GridItemType.Temperature -> {
                    TemperatureItem(
                        highTemp = (item.data?.let { (it as Pair<*, *>).first } ?: "").toString(),
                        lowTemp = (item.data?.let { (it as Pair<*, *>).second } ?: "").toString()
                    )
                }
                GridItemType.UVIndex -> {
                    UVIndexItem(currentIndex = item.data as Int)
                }
                GridItemType.Humidity -> {
                    OtherWeatherItem(title = "Humidity", value = item.data as String)
                }
                GridItemType.Wind -> {
                    OtherWeatherItem(title = "Wind", value = item.data as String)
                }
                GridItemType.Precipitation -> {
                    OtherWeatherItem(title = "Precipitation", value = item.data as String)
                }
                GridItemType.Pressure -> {
                    OtherWeatherItem(title = "Pressure", value = item.data as String)
                }
            }
        }
    }
}


@Composable
fun TemperatureItem(highTemp: String, lowTemp: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Text(text = "Feels Like")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.high),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
            )
            Text(text = highTemp, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                painter = painterResource(id = R.drawable.low),
                contentDescription = null,
                modifier = Modifier
                    .size(14.dp)
                )
            Text(text = lowTemp, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun UVIndexItem(currentIndex: Int) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "UV index", style = MaterialTheme.typography.bodyLarge)
        UVIndexIndicator(currentIndex)
    }
}

@Composable
fun OtherWeatherItem(title: String, value: String) {
    // UI for other weather information like humidity, wind, etc.
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodySmall)
    }
}


@Composable
fun UVIndexIndicator(currentIndex: Int) {
    val uvColors = listOf(
        Color(0xFF00E676), // Green
        Color(0xFFFFEB3B), // Yellow
        Color(0xFFFFA726), // Orange
        Color(0xFFFF5252), // Red
        Color(0xFFD32F2F)  // Dark Red
    )

    val uvRanges = listOf(2, 5, 7, 10, 11) // End of ranges for color segmentation

    val currentColor = when (currentIndex) {
        in 0..2 -> uvColors[0]
        in 3..5 -> uvColors[1]
        in 6..7 -> uvColors[2]
        in 8..10 -> uvColors[3]
        else -> uvColors[4]
    }

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(40.dp)
        .padding(16.dp)
    ) {
        val canvasWidth = size.width
        val segmentWidth = canvasWidth / 10

        // Draw the segmented line with color changes
        uvRanges.forEachIndexed { index, range ->
            drawLine(
                color = uvColors[index],
                start = Offset(if (index == 0) 0f else uvRanges[index - 1] * segmentWidth, size.height / 2),
                end = Offset(range * segmentWidth, size.height / 2),
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(segmentWidth - 10f, 10f), 0f)
            )
        }

        // Draw the current index number at its position directly along the line
        drawIntoCanvas { canvas ->
            val textPaint = android.graphics.Paint().apply {
                color = currentColor.toArgb()
                textSize = 16.sp.toPx()
                textAlign = android.graphics.Paint.Align.CENTER

            }

            canvas.nativeCanvas.drawText(
                currentIndex.toString(),
                currentIndex * segmentWidth,
                size.height / 2 + 6.dp.toPx(), // Align the text along the line
                textPaint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UVIndexIndicatorPreview(){
    MaterialTheme {
        UVIndexIndicator(8)

    }
}

@Preview(showBackground = true)
@Composable
fun WeatherItemsPreview(){
    // Sample data
    val gridItems = listOf(
        GridItem(GridItemType.Temperature, Pair("30°", "15°")),
        GridItem(GridItemType.UVIndex, 8), 
        GridItem(GridItemType.Humidity, "70%"),
        GridItem(GridItemType.Wind, "15 km/h"),
        GridItem(GridItemType.Precipitation, "5 mm"),
        GridItem(GridItemType.Pressure, "1013 hPa") 
    )

    WeatherGridItems(items = gridItems)

    MaterialTheme {
        WeatherGridItems(gridItems)
    }
}
package com.joel.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joel.home.R
import com.joel.models.ForecastInfo
import com.joel.models.GridItem
import com.joel.models.GridItemType
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LocationWeatherDetails(
    forecastInfo: ForecastInfo,
    showMoreInfo: Boolean,
    items: List<GridItem>,
    currentTime: LocalDateTime
) {

    val hourlyFormat = DateTimeFormatter.ofPattern("hh:mm a")
    val dailyFormat = DateTimeFormatter.ofPattern("EEE, MMM d")
    val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    val sunriseTime = LocalDateTime.parse(forecastInfo.dailyForeCast.first().sunrise, timeFormat)
    val sunsetTime = LocalDateTime.parse(forecastInfo.dailyForeCast.first().sunset, timeFormat)

    val dayDuration = Duration.between(sunriseTime, sunsetTime).toHours().toInt()
    val nightDuration = (24 - dayDuration).toInt()

    val isDayTime = currentTime.isAfter(sunriseTime) && currentTime.isBefore(sunsetTime)

    val currentTimeInHours = currentTime.hour + currentTime.minute / 60f
    val currentHour = currentTime.hour

    val currentForecast = forecastInfo.hourlyForecast.find {
        LocalDateTime.parse(it.time, DateTimeFormatter.ISO_DATE_TIME).hour == currentHour
    }

    Column {
        currentForecast?.let { hourlyForecast ->
            val dateTime = LocalDateTime.parse(hourlyForecast.time, DateTimeFormatter.ISO_DATE_TIME)
            val formattedTime = currentTime.format(hourlyFormat)
            val formattedDate = dateTime.format(dailyFormat)

            if (showMoreInfo) {
                MoreWeatherDetails(
                    date = formattedDate,
                    time = formattedTime,
                    weather = hourlyForecast.weather.weatherDesc,
                    temperature = hourlyForecast.temp.toFloat().toInt(),
                    weatherIcon = hourlyForecast.weather.iconRes,
                    items = items,
                    sunrise = sunriseTime.format(hourlyFormat),
                    sunset = sunsetTime.format(hourlyFormat),
                    dayDuration = dayDuration.toString(),
                    nightDuration = nightDuration.toString(),
                    currentTime = currentTimeInHours,
                    isDayTime = isDayTime
                )
            } else {
                LessWeatherDetails(
                    date = formattedDate,
                    time = formattedTime,
                    weather = hourlyForecast.weather.weatherDesc,
                    temperature = hourlyForecast.temp.toFloat().toInt(),
                    weatherIcon = hourlyForecast.weather.iconRes
                )
            }
        }
    }
}




@Composable
fun MoreWeatherDetails(
    date: String,
    time: String,
    weather: String,
    temperature: Int,
    weatherIcon: Int,
    items: List<GridItem>,
    sunrise: String,
    sunset: String,
    dayDuration: String, nightDuration: String, currentTime: Float, isDayTime: Boolean
){

    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 4.dp),
    ){
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF132847), Color(0xFF142036))
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(14.dp)
            ) {
                Column() {
                    Text(text = "$date, $time", color = Color.White)
                    TemperatureDisplay(
                        temperature = temperature,
                        unit = "°C",
                        temperatureFontSize = 80.sp,
                        unitFontSize = 24.sp,
                    )
                    Text(weather, color = Color.White)
                }
                Icon(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .size(170.dp)
                        .align(Alignment.CenterVertically),
                    tint = Color.White
                )
            }
            SunriseSunsetDisplay(sunrise, sunset, dayDuration, nightDuration, currentTime, isDayTime)
            WeatherGridItems(items)
        }
    }
}

@Composable
fun LessWeatherDetails(
    date: String,
    time: String,
    weather: String,
    temperature: Int,
    weatherIcon: Int
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 4.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = Color.Transparent
            )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF132847), Color(0xFF142036))
                    )
                )
                .padding(14.dp)
        ) {
            Column() {
                Text(text = "$date, $time", color = Color.White)
                TemperatureDisplay(
                    temperature = temperature,
                    unit = "°C",
                    temperatureFontSize = 80.sp,
                    unitFontSize = 24.sp
                )
                Text(weather, color = Color.White)
            }
            Icon(
                painter = painterResource(id = weatherIcon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 24.dp)
                    .size(170.dp)
                    .align(Alignment.CenterVertically),
                tint = Color.White
            )
        }
    }
}

@Composable
fun SunriseSunsetDisplay(sunrise: String, sunset: String, dayDuration: String, nightDuration: String, currentTime: Float, isDayTime: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text("Sunrise", style = MaterialTheme.typography.bodySmall)
            Text(sunrise, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.width(16.dp))

//        DomeTracker(dayDuration = dayDuration.toFloat(), nightDuration = nightDuration.toFloat(), currentTime = currentTime, isDayTime = isDayTime)
        DomeTracker(dayDuration = dayDuration.toFloat(), nightDuration = nightDuration.toFloat(), currentTime = currentTime, isDayTime = isDayTime)

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text("Sunset", style = MaterialTheme.typography.bodySmall)
            Text(sunset, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun DomeTracker(dayDuration: Float, nightDuration: Float, currentTime: Float, isDayTime: Boolean) {
    val dayColor = Color.Yellow
    val nightColor = Color.DarkGray
    val iconColor = if (isDayTime) Color.Yellow else Color(0xFFA0A0A0)

    // Calculate current progress
    val totalDuration = if (isDayTime) dayDuration * 60 else nightDuration * 60
    val currentMinutes = currentTime * 60

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
    ) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val domeSize = size / 2f
            val strokeWidth = 10.dp.toPx() // Use toPx() if using Canvas for Stroke
            val radius = domeSize.width

            // Background arc (dotted line)
            drawArc(
                color = Color.Gray,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            )

            // Covered time arc (continuous)
            val coveredSweepAngle = 180f * (currentMinutes / totalDuration)
            drawArc(
                color = if (isDayTime) dayColor else nightColor,
                startAngle = 180f,
                sweepAngle = coveredSweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // Start and end circles
            drawCircle(color = iconColor, radius = strokeWidth, center = Offset(domeSize.width, domeSize.height))
            drawCircle(
                color = iconColor,
                radius = strokeWidth,
                center = Offset(domeSize.width + radius * Math.cos(Math.PI).toFloat(), domeSize.height)
            )
        }

        // Move the icon along the curve
        val angle = 180 * (currentMinutes / totalDuration)
        val iconX = (100 + 100 * cos(Math.toRadians(angle.toDouble()))).toFloat()
        val iconY = (100 - 100 * sin(Math.toRadians(angle.toDouble()))).toFloat() // Invert Y for top arc

        Box(
            modifier = Modifier
                .size(40.dp)
                .offset { IntOffset(iconX.toInt() - 20, iconY.toInt() - 20) } // Adjust position to align
//                .background(iconColor, CircleShape)
        ) {
            Icon(
                painter = painterResource(if (isDayTime) R.drawable.round_light_24 else R.drawable.round_dark_mode_24),
                contentDescription = if (isDayTime) "Sun" else "Crescent",
                tint = Color.Unspecified,
                modifier = Modifier.size(40.dp)
            )
        }

        val displayTime = if (isDayTime) {
            "${(currentMinutes / 60).toInt()}h ${(currentMinutes % 60).toInt()}m"
        } else {
            "${(currentMinutes / 60).toInt()}h ${(currentMinutes % 60).toInt()}m"
        }

        Text(
            text = displayTime,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherRowDayPreview(){
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val sunrise = LocalDateTime.of(2024, 7, 30, 6, 0)
    val sunset = LocalDateTime.of(2024, 7, 30, 18, 0)
    val currentTime = LocalDateTime.of(2024, 7, 30, 12, 0)

    val dayDuration = Duration.between(sunrise, sunset)
    val nightDuration = Duration.between(sunset, sunrise.plusDays(1))

    val isDayTime = currentTime.isAfter(sunrise) && currentTime.isBefore(sunset)
    val currentMinutes = Duration.between(if (isDayTime) sunrise else sunset, currentTime).toMinutes().toFloat()

    SunriseSunsetDisplay(
        sunrise = sunrise.format(formatter),
        sunset = sunset.format(formatter),
        dayDuration = dayDuration.toString(),
        nightDuration = nightDuration.toString(),
        currentTime = currentMinutes,
        isDayTime = isDayTime
    )

}


@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LessDetailsPreview(){
    MaterialTheme {
        LessWeatherDetails(
//            day = "Sun",
            date = "Sun, 23 April",
            time = "12:05",
            weather = "Partially Cloudy",
            temperature = 15,
            weatherIcon = R.drawable.cloudy_1_day
        )
    }

}

@Composable
fun TemperatureDisplay(temperature: Int, unit: String, temperatureFontSize: TextUnit, unitFontSize: TextUnit) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$temperature",
            fontSize = temperatureFontSize,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = unit,
                fontSize = unitFontSize,
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TemperatureDisplayPreview() {
    MaterialTheme {
        Surface {
            TemperatureDisplay(
                temperature = 15,
                unit = "°C",
                temperatureFontSize = 80.sp,
                unitFontSize = 24.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoreWeatherDetailsPreview(){
    // Sample data
    val gridItems = listOf(
        GridItem(GridItemType.Temperature, Pair("30°", "15°")),
        GridItem(GridItemType.UVIndex, 8),
        GridItem(GridItemType.Humidity, "70%"),
        GridItem(GridItemType.Wind, "15 km/h"),
        GridItem(GridItemType.Precipitation, "5 mm"),
        GridItem(GridItemType.Pressure, "1013 hPa")
    )

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    val sunrise = LocalDateTime.of(2024, 7, 30, 6, 0)
    val sunset = LocalDateTime.of(2024, 7, 30, 18, 0)
    val currentTime = LocalDateTime.of(2024, 7, 30, 12, 0)

    val dayDuration = Duration.between(sunrise, sunset)
    val nightDuration = Duration.between(sunset, sunrise.plusDays(1))

    val isDayTime = currentTime.isAfter(sunrise) && currentTime.isBefore(sunset)
    val currentMinutes = Duration.between(if (isDayTime) sunrise else sunset, currentTime).toMinutes().toFloat()



    MaterialTheme {
        MoreWeatherDetails(
            date = "Sun, 23 April",
            time = "12:05",
            weather = "Partially Cloudy",
            temperature = 15,
            weatherIcon = R.drawable.cloudy_1_day,
            items = gridItems,
            sunrise = sunrise.format(formatter),
            sunset = sunset.format(formatter),
            dayDuration = dayDuration.toString(),
            nightDuration = nightDuration.toString(),
            currentTime = currentMinutes,
            isDayTime = isDayTime
        )
    }
}
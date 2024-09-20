package com.joel.home.contents

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joel.home.R
import com.joel.home.vm.TimeViewModel
import com.joel.models.ForecastInfo
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LocationWeatherDetails(
    forecastInfo: ForecastInfo,
    showMoreInfo: Boolean,
    items: List<GridItem>,
    viewModel: TimeViewModel = viewModel()
) {
    val currentTime by viewModel.currentTime

    val hourlyFormat = DateTimeFormatter.ofPattern("hh:mm a")
    val dailyFormat = DateTimeFormatter.ofPattern("EEE, MMM d")
    val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    val sunriseTime = LocalDateTime.parse(forecastInfo.dailyForeCast.first().sunrise, timeFormat)
    val sunsetTime = LocalDateTime.parse(forecastInfo.dailyForeCast.first().sunset, timeFormat)

    val dayDuration = Duration.between(sunriseTime, sunsetTime).toHours()
    val nightDuration = 24 - dayDuration

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
                    day = dateTime.dayOfWeek.name,
                    date = formattedDate,
                    time = formattedTime,
                    weather = hourlyForecast.weather.weatherDesc,
                    temperature = hourlyForecast.temp.toFloat().toInt(),
                    weatherIcon = hourlyForecast.weather.iconRes,
                    items = items,
                    sunrise = sunriseTime.format(hourlyFormat),
                    sunset = sunsetTime.format(hourlyFormat),
                    dayDuration = "$dayDuration hours",
                    nightDuration = "$nightDuration hours",
                    currentTime = currentTimeInHours,
                    isDayTime = isDayTime
                )
            }
            else {
                LessWeatherDetails(
//                    day = dateTime.dayOfWeek.name,
                    date = formattedDate,
                    time = formattedTime,
                    weather = hourlyForecast.weather.weatherDesc,
                    temperature = hourlyForecast.temp.toFloat().toInt(),
                    weatherIcon = hourlyForecast.weather.iconRes
                )
            }
        }

//        forecastInfo.hourlyForecast.forEach { hourlyForecast ->
//            val dateTime = LocalDateTime.parse(hourlyForecast.time, DateTimeFormatter.ISO_DATE_TIME)
//            val formattedTime = currentTime.format(hourlyFormat)
//            val formattedDate = dateTime.format(dailyFormat)
//
//            if (showMoreInfo) {
//                MoreWeatherDetails(
//                    day = dateTime.dayOfWeek.name,
//                    date = formattedDate,
//                    time = formattedTime,
//                    weather = hourlyForecast.weather.weatherDesc,
//                    temperature = hourlyForecast.temp.toFloat().toInt(),
//                    weatherIcon = hourlyForecast.weather.iconRes,
//                    items = items,
//                    sunrise = sunriseTime.format(hourlyFormat),
//                    sunset = sunsetTime.format(hourlyFormat),
//                    dayDuration = "$dayDuration hours",
//                    nightDuration = "$nightDuration hours",
//                    currentTime = currentTimeInHours,
//                    isDayTime = isDayTime
//                )
//            }
//            else {
//                LessWeatherDetails(
////                    day = dateTime.dayOfWeek.name,
//                    date = formattedDate,
//                    time = formattedTime,
//                    weather = hourlyForecast.weather.weatherDesc,
//                    temperature = hourlyForecast.temp.toFloat().toInt(),
//                    weatherIcon = hourlyForecast.weather.iconRes
//                )
//            }
//        }
   }
}



@Composable
fun MoreWeatherDetails(
    day: String,
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

    Column {
        LessWeatherDetails(
//            day = day,
            date = date,
            time = time,
            weather = weather,
            temperature = temperature,
            weatherIcon = weatherIcon
        )
        SunriseSunsetDisplay(sunrise, sunset, dayDuration, nightDuration, currentTime, isDayTime)
        WeatherGridItems(items)
    }
}

@Composable
fun LessWeatherDetails(
//    day: String,
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(14.dp)
        ) {
            Column() {
                Text(text = "$date, $time")
                TemperatureDisplay(
                    temperature = temperature,
                    unit = "°C",
                    temperatureFontSize = 80.sp,
                    unitFontSize = 24.sp
                )
                Text(weather)
            }
            Icon(
                painter = painterResource(id = weatherIcon),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 24.dp)
                    .size(170.dp)
                    .align(Alignment.CenterVertically)
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

        DomeTracker(dayDuration, nightDuration, currentTime, isDayTime)

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
fun DomeTracker(dayDuration: String, nightDuration: String, currentTime: Float, isDayTime: Boolean) {
    val dayColor = Color.Yellow
    val nightColor = Color.DarkGray
    val sunIconColor = Color.Yellow
    val crescentIconColor = Color(0xFFA0A0A0) // Slightly dark color for crescent

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(Color.LightGray, CircleShape)
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val domeSize = size / 2f
            val strokeWidth = 8.dp.toPx()
            val radius = domeSize.width / 2

            // Draw background arc (dotted line)
            drawArc(
                color = Color.DarkGray,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeWidth, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
            )

            // Draw covered time arc (continuous line)
            val sweepAngle = 180f * (currentTime / (if (isDayTime) dayDuration.toFloat() else nightDuration.toFloat()))
            drawArc(
                color = if (isDayTime) dayColor else nightColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .background(if (isDayTime) sunIconColor else crescentIconColor, CircleShape)
        ) {
            Icon(
                painter = painterResource(if (isDayTime) R.drawable.round_light_24 else R.drawable.round_dark_mode_24),
                contentDescription = if (isDayTime) "Sun" else "Crescent",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(if (isDayTime) "Daylight" else "Nighttime", fontSize = 12.sp)
            Text(if (isDayTime) dayDuration else nightDuration, fontSize = 10.sp)
        }
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
            date = "23 April",
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
            fontSize = temperatureFontSize
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = unit,
                fontSize = unitFontSize
            )
        }
    }
}


@Preview(showBackground = true)
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
            day = "Sun",
            date = "23 April",
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
package com.joel.home.contents

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joel.models.ForecastInfo
import com.joel.models.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun HourlyForecast(
    hourlyForecastItems: List<ForecastInfo.HourlyForecast>,
    currentTime: LocalDateTime
) {
    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    val filteredForecasts = hourlyForecastItems.filter { forecast ->
        val forecastTime = LocalDateTime.parse(forecast.time, inputFormat)
        forecastTime.isAfter(currentTime) || forecastTime.hour == currentTime.hour
    }.toMutableList()

    val currentHourForecastIndex = filteredForecasts.indexOfFirst {
        LocalDateTime.parse(it.time, inputFormat).hour == currentTime.hour
    }

    if (currentHourForecastIndex != -1) {
        val currentHourForecast = filteredForecasts.removeAt(currentHourForecastIndex)
        filteredForecasts.add(0, currentHourForecast.copy(temp = currentHourForecast.temp, time = currentHourForecast.time, weather = currentHourForecast.weather))
    }

    LazyRow(
        contentPadding = PaddingValues(12.dp)
    ) {
        items(filteredForecasts) { forecast ->
            HourlyItemForecast(hourlyForecast = forecast, currentTime)
        }
    }
}



@Composable
fun HourlyItemForecast(
    hourlyForecast : ForecastInfo.HourlyForecast,
    currentTime: LocalDateTime
){

    val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
    val outputFormat = DateTimeFormatter.ofPattern("hh:mm a")
    val forecastTime = try {
        LocalDateTime.parse(hourlyForecast.time, inputFormat)
    } catch (e: Exception) {
        null
    }

    val isCurrentHour = forecastTime?.hour == currentTime.hour

    val displayTime = if (isCurrentHour) {
        "Now"
    } else {
        forecastTime?.format(outputFormat) ?: "__:__"
    }



    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp),
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 5.dp,
            ),
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF132847)
            )
        ) {
            Icon(
                painter = painterResource(id = hourlyForecast.weather.iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp),
                tint = Color.White
            )
        }
        Text(
            text = displayTime.uppercase(Locale.getDefault()),
            fontSize = 10.sp, color = Color.White
        )

        Text(text = "${hourlyForecast.temp}°", fontSize = 8.sp, color = Color.White)
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HourlyItemForecastPreview(){
    val hourlyForecast = ForecastInfo.HourlyForecast(
        time = "2024-10-02T16:00",
        humidity = 67,
        isDay = 1,
        pressure = 234,
        visibility = 45,
        temp = "12",
        wind = 10,
        weather = WeatherType.fromWMO(2)

    )
    MaterialTheme {
        HourlyItemForecast(hourlyForecast, LocalDateTime.now())
    }

}
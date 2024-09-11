package com.joel.home.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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

@Composable
fun HourlyForecast(
    hourlyForecastItems: List<ForecastInfo.HourlyForecast>
) {
    LazyRow {
        items(hourlyForecastItems) { forecast ->
            HourlyItemForecast(hourlyForecast = forecast)
        }
    }
}


@Composable
fun HourlyItemForecast(
    hourlyForecast : ForecastInfo.HourlyForecast
){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
    ) {
        Card(
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 5.dp,
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(
                painter = painterResource(id = hourlyForecast.weather.iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .padding(4.dp)
            )
        }
        Text(
            text = hourlyForecast.time,
            color = Color.Black.copy(0.6f),
            fontSize = 8.sp
        )

        Text(text = "${hourlyForecast.temp}°", fontSize = 8.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun HourlyItemForecastPreview(){
    val hourlyForecast = ForecastInfo.HourlyForecast(
        time = "19:00",
        humidity = 67,
        isDay = 1,
        pressure = 234,
        visibility = 45,
        temp = "12",
        wind = 10,
        weather = WeatherType.fromWMO(2)

    )
    MaterialTheme {
        HourlyItemForecast(hourlyForecast)
    }

}
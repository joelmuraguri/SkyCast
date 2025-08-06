package com.joe.locations.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joe.models.R
import com.joe.home.contents.TemperatureDisplay
import com.joe.home.contents.TemperatureItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun LocationItemCard(
    time: String,
    locationName: String,
    weatherDescription: String,
    highTemp: String,
    lowTemp: String,
    temperature: Int,
    weatherIcon: Int
) {

    val formattedTime = try {
        LocalDateTime.parse(time).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        time // fallback in case of parse error
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFF132847), Color(0xFF142036))
                    )
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left section
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = formattedTime,
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
                Text(
                    text = locationName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = weatherDescription,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // Right section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Icon(
                    painter = painterResource(id = weatherIcon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp),
                )
            }
        }
    }
}

@Composable
fun FirstPartLayout(
    time : String,
    locationName : String,
    weatherDescription : String
){

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time,
            color = Color.LightGray,
            fontSize = 16.sp
        )
        Text(
            text = locationName,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = weatherDescription,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Composable
fun SecondPartLayout(
    highTemp: String,
    lowTemp: String,
    temperature: Int,
    weatherIcon : Int
){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            com.joe.home.contents.TemperatureDisplay(
                temperature = temperature,
                unit = "°C",
                temperatureFontSize = 32.sp,
                unitFontSize = 14.sp
            )
            TemperatureItem(highTemp, lowTemp)
        }

        Icon(
            painter = painterResource(id = weatherIcon),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 24.dp)
                .size(50.dp)
                .align(Alignment.CenterVertically),
            tint = Color.White
        )
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FirstPartLayoutPreview() {
    MaterialTheme {
        FirstPartLayout(
            time = "11:05",
            locationName = "Jabriya",
            weatherDescription = "Partially Cloudy"
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SecondPartLayoutPreview() {
    MaterialTheme {
        SecondPartLayout(
            highTemp = "34°",
            lowTemp = "19°",
            temperature = 23,
            weatherIcon = R.drawable.ic_sunny
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LocationItemCardPreview() {
    MaterialTheme {
        LocationItemCard(
            highTemp = "34°",
            lowTemp = "19°",
            temperature = 23,
            weatherIcon = R.drawable.ic_sunny,
            time = "11:05",
            locationName = "Jabriya",
            weatherDescription = "Partially Cloudy"
        )
    }
}
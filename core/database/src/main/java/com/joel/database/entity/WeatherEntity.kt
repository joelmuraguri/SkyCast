package com.joel.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "weather_forecast")
data class WeatherEntity(
    @PrimaryKey val locationName: String,
    val longitude: Double,
    val latitude: Double,
    val dailyForecast: String, // JSON String of List<Weather.Daily>
    val hourlyForecast: String, // JSON String of List<Weather.Hourly>
    val timeStamp : Long
)

@Serializable
data class DailyEntity(
    val uv: Int,
    val date : String,
    val weather : Int,
    val lowTemp : Int,
    val highTemp : Int,
    val sunrise : String,
    val sunset : String
)

@Serializable
data class HourlyEntity(
    val time : String,
    val temp : String,
    val weather : Int,
    val wind : Int,
    val pressure : Int,
    val visibility: Int,
    val isDay : Int,
    val humidity : Int
)
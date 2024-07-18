package com.joel.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_forecast")
data class WeatherEntity(
    @PrimaryKey val locationName: String,
    val longitude: Double,
    val latitude: Double,
    val dailyForecast: String, // JSON String of List<Weather.Daily>
    val hourlyForecast: String // JSON String of List<Weather.Hourly>
)

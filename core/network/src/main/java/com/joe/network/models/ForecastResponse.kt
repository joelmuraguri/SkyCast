package com.joe.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val daily: Daily,
    @SerialName("daily_units")
    val dailyUnits: DailyUnits,
    val elevation: Double,
    @SerialName("generationtime_ms")
    val generationTime: Double,
    val hourly: Hourly,
    @SerialName("hourly_units")
    val hourlyUnits: HourlyUnits,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    @SerialName("timezone_abbreviation")
    val timezoneAbbreviation: String,
    @SerialName("utc_offset_seconds")
    val utc: Int
){
    @Serializable
    data class Daily(
        val sunrise: List<String> = emptyList(),
        val sunset: List<String> = emptyList(),
        @SerialName("temperature_2m_max")
        val temperatureMax: List<Double> = emptyList(),
        @SerialName("temperature_2m_min")
        val temperatureMin: List<Double> = emptyList(),
        val time: List<String> = emptyList(),
        @SerialName("uv_index_clear_sky_max")
        val uvIndex: List<Double> = emptyList(),
        @SerialName("weather_code")
        val weatherCode: List<Int> = emptyList()
    )

    @Serializable
    data class DailyUnits(
        val sunrise: String,
        val sunset: String,
        @SerialName("temperature_2m_max")
        val temperatureMax: String,
        @SerialName("temperature_2m_min")
        val temperatureMin: String,
        val time:String,
        @SerialName("uv_index_clear_sky_max")
        val uvIndex: String,
        @SerialName("weather_code")
        val weatherCode: String
    )

    @Serializable
    data class Hourly(
        @SerialName("apparent_temperature")
        val apparentTemperature: List<Double> = emptyList(),
        @SerialName("is_day")
        val isDay: List<Int> = emptyList(),
        @SerialName("relative_humidity_2m")
        val humidity: List<Int>,
        @SerialName("surface_pressure")
        val surfacePressure: List<Double> = emptyList(),
        val time: List<String> = emptyList(),
        val visibility: List<Double> = emptyList(),
        @SerialName("weather_code")
        val weatherCode: List<Int> = emptyList(),
        @SerialName("wind_speed_10m")
        val windSpeed: List<Double> = emptyList()
    )
    
    @Serializable
    data class HourlyUnits(
        @SerialName("apparent_temperature")
        val apparentTemperature: String,
        @SerialName("is_day")
        val isDay: String,
        @SerialName("relative_humidity_2m")
        val humidity: String,
        @SerialName("surface_pressure")
        val surfacePressure: String,
        val time: String,
        val visibility: String,
        @SerialName("weather_code")
        val weatherCode: String,
        @SerialName("wind_speed_10m")
        val windSpeed: String
    )
}
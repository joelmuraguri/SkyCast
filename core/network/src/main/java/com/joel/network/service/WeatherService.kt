package com.joel.network.service

import com.joel.network.models.ForecastResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    suspend fun forecast(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude : Double,
        @Query("hourly") hourly : List<String> = hourlyParams,
        @Query("forecast_days") forecastDays: Int = 7,
        @Query("daily") daily : List<String> = dailyParams,
        @Query("forecast_hours") forecastHours : Int = 24
    ) : ApiResponse<ForecastResponse>
}

val hourlyParams = listOf("is_day","weather_code","wind_speed_10m","surface_pressure","relative_humidity_2m","visibility","apparent_temperature")
val dailyParams = listOf("temperature_2m_max","temperature_2m_min","sunrise","sunset","uv_index_clear_sky_max","weather_code")

/***
val url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m,visibility,is_day,weather_code,wind_speed_10m,surface_pressure,relative_humidity_2m,visibility,apparent_temperature,temperature_2m,dew_point_2m&forecast_days=7&current&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,uv_index_clear_sky_max,weather_code"
***/
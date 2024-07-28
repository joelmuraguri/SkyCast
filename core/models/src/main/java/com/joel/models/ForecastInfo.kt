package com.joel.models


data class ForecastInfo(
    val location : Location,
    val dailyForeCast : List<DailyForecast>,
    val hourlyForecast : List<HourlyForecast>,
    val name : String,
){
    data class DailyForecast(
        val uv: Int,
        val date : String,
        val weather : WeatherType,
        val lowTemp : Int,
        val highTemp : Int,
        val sunrise : String,
        val sunset : String
    )

    data class HourlyForecast(
        val time : String,
        val temp : String,
        val weather : WeatherType,
        val wind : Int,
        val pressure : Int,
        val visibility: Int,
        val isDay : Int,
        val humidity : Int
    )
}

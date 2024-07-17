package com.joel.models

data class Weather(
    val location : Location,
    val dailyForeCast : List<Daily>,
    val hourlyForecast : List<Hourly>,
    val name : String,
    val lowTemp : Int,
    val highTemp: Int,
    val weather: Int,
    val uv : Int,
    val temp : Int,
    val humidity : Int,
    val wind : Int,
    val pressure : Int,
    val visibility: Int,
    val isDay : Int,
    val sunrise : String,
    val sunset : String
){

    data class Daily(
        val date : String,
        val day : String,
        val weather : Int,
        val lowTemp : Int,
        val highTemp : Int
    )

    data class Hourly(
        val time : String,
        val temp : String,
        val weather : Int
    )
}

data class Location(
    val longitude : Long,
    val latitude : Long
)

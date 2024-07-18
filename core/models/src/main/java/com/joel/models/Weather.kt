package com.joel.models

data class Weather(
    val location : Location,
    val dailyForeCast : List<Daily>,
    val hourlyForecast : List<Hourly>,
    val name : String,
){
    data class Daily(
        val uv: Int,
        val date : String,
        val weather : Int,
        val lowTemp : Int,
        val highTemp : Int,
        val sunrise : String,
        val sunset : String
    )

    data class Hourly(
        val time : String,
        val temp : String,
        val weather : Int,
        val wind : Int,
        val pressure : Int,
        val visibility: Int,
        val isDay : Int,
        val humidity : Int
    )
}

data class Location(
    val longitude : Double,
    val latitude : Double
)

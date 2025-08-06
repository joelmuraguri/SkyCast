package com.joe.supabase.favourites

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavouriteLocation(
    val id: String, // Supabase UUID
    @SerialName("user_id")
    val userId: String, // From Supabase auth
    @SerialName("city_name")
    val locationName: String,
    val latitude: Float,
    val longitude: Float,
    @SerialName("weather_description")
    val weatherDescription: String,
    @SerialName("current_temperature")
    val currentTemperature: Int,
    @SerialName("high_temp")
    val highTemp: Int,
    @SerialName("low_temp")
    val lowTemp: Int,
//    @SerialName("icon_code")
//    val iconCode: String, // Icon resource name or API icon code
//    @SerialName("last_updated")
//    val lastUpdated: String, // ISO 8601 formatted timestamp
    @SerialName("time")
    val time : String,
    @SerialName("weather_code")
    val weatherCode : Int
)

package com.joel.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favourite_place")
data class FavouritePlace(
    val longitude: Double,
    val latitude: Double,
    @PrimaryKey val locationName : String,
    val country : String,
    val timeZone : String,
    val countryCode : String,
    val admin : String
)



package com.joel.models

data class Place(
    val location: Location,
    val name : String,
    val country : String,
    val timeZone : String,
    val countryCode : String
)

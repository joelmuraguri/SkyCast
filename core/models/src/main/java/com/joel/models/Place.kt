package com.joel.models

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val location: Location,
    val name : String,
    val country : String,
    val timeZone : String,
    val countryCode : String,
    val admin : String
)

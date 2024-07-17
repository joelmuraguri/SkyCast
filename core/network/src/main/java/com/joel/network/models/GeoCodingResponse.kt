package com.joel.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingResponse(
    @SerialName("generationtime_ms")
    val generationTimeMs: Double,
    val results: List<Place>
){
    @Serializable
    data class Place(
        val admin1: String,
        @SerialName("admin1_id")
        val admin1Id: Int,
        val admin2: String,
        @SerialName("admin2_id")
        val admin2Id: Int,
        val country: String,
        @SerialName("country_code")
        val countryCode: String,
        @SerialName("country_id")
        val countryId: Int,
        val elevation: Double,
        @SerialName("feature_code")
        val featureCode: String,
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val name: String,
        val population: Int,
        val timezone: String
    )
}
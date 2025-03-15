package com.joe.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingResponse(
    @SerialName("generationtime_ms")
    val generationTimeMs: Double ?= null,
    val results: List<Place> = emptyList()
){
    @Serializable
    data class Place(
        val admin1: String ?= null,
        @SerialName("admin1_id")
        val admin1Id: Int ?= null,
        val admin2: String ? = null,
        @SerialName("admin2_id")
        val admin2Id: Int ?= null,
        val country: String ?= null,
        @SerialName("country_code")
        val countryCode: String ?= null,
        @SerialName("country_id")
        val countryId: Int ?= null,
        val elevation: Double ?= null,
        @SerialName("feature_code")
        val featureCode: String ?= null,
        val id: Int ?= null,
        val latitude: Double ?= null,
        val longitude: Double ?= null,
        val name: String ? = null,
        val population: Int ?= null,
        val timezone: String ?= null
    )
}
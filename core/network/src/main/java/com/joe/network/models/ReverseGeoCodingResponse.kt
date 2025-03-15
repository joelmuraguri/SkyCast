package com.joe.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeoCodingResponse(
    val address: Address,
    @SerialName("boundingbox")
    val boundingBox: List<String> = emptyList(),
    @SerialName("display_name")
    val displayName: String ?= null,
    val lat: String ?= null,
    val licence: String ?= null,
    val lon: String ?= null,
    @SerialName("osm_id")
    val osmId: Int ?= null,
    @SerialName("osm_type")
    val osmType: String ?= null,
    @SerialName("placeId")
    val placeId: Int ?= null
){

    @Serializable
    data class Address(
        @SerialName("ISO3166-2-lvl4")
        val iso: String ?= null,
        val city: String ?= null,
        @SerialName("city_district")
        val cityDistrict: String ?= null,
        val country: String ?= null,
        @SerialName("country_code")
        val countryCode: String ?= null,
        val postcode: String ?= null,
        val state: String ?= null,
        val suburb: String ?= null,
        val amenity: String ?= null,
        val building: String ?= null,
        val county : String ?= null
    )
}
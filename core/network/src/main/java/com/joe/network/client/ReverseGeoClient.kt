package com.joe.network.client

import com.joe.network.models.ReverseGeoCodingResponse
import com.joe.network.service.ReverseGeoService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ReverseGeoClient @Inject constructor(
    private val service: ReverseGeoService
) {
    suspend fun getLocationAddress(lat : Double, long : Double) : ApiResponse<ReverseGeoCodingResponse> {
        return service.getLocationAddress(latitude =lat, longitude = long)
    }
}
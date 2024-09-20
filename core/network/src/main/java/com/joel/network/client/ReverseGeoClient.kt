package com.joel.network.client

import com.joel.network.models.ReverseGeoCodingResponse
import com.joel.network.service.ReverseGeoService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class ReverseGeoClient @Inject constructor(
    private val service: ReverseGeoService
) {
    suspend fun getLocationAddress(lat : Double, long : Double) : ApiResponse<ReverseGeoCodingResponse> {
        return service.getLocationAddress(latitude =lat, longitude = long)
    }
}
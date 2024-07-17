package com.joel.network.client

import com.joel.network.models.GeoCodingResponse
import com.joel.network.service.GeoService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class GeoClient @Inject constructor(
    private val service: GeoService
) {
    suspend fun locationSearch(name : String) : ApiResponse<GeoCodingResponse>{
        return service.locationSearch(name)
    }
}
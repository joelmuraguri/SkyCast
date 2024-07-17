package com.joel.network.client

import com.joel.network.models.ForecastResponse
import com.joel.network.service.WeatherService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class WeatherClient @Inject constructor(
    private val service: WeatherService
) {
    suspend fun forecast(latitude : Long, longitude : Long) : ApiResponse<ForecastResponse>{
        return service.forecast(latitude, longitude)
    }
}
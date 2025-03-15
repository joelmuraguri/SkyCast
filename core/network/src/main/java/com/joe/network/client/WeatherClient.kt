package com.joe.network.client

import com.joe.network.models.ForecastResponse
import com.joe.network.service.WeatherService
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class WeatherClient @Inject constructor(
    private val service: WeatherService
) {
    suspend fun forecast(latitude : Double, longitude : Double) : ApiResponse<ForecastResponse>{
        return service.forecast(latitude, longitude)
    }
}
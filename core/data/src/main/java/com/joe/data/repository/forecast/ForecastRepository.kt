package com.joe.data.repository.forecast

import androidx.annotation.WorkerThread
import com.joe.models.WeatherDomain
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    @WorkerThread
    fun fetchWeatherForecast(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<WeatherDomain>
}
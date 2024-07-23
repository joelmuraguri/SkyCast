package com.joel.data.repository.forecast

import androidx.annotation.WorkerThread
import com.joel.models.Location
import com.joel.models.Weather
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    @WorkerThread
    fun fetchWeatherForecast(
        location: Location,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<Weather>
}
package com.joel.data.repository.location

import androidx.annotation.WorkerThread
import com.joel.data.utils.Resource
import com.joel.models.Place
import com.joel.models.WeatherDomain
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    @WorkerThread
    fun locationsSearch(
        query : String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<List<Place>>

    @WorkerThread
    fun fetchLocationForecast(
        latitude : Double, longitude : Double,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit,
    ): Flow<WeatherDomain>
}

package com.joel.data.repository.location

import com.joel.models.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun fetchCurrentLocation(): Flow<Location>
    class LocationException(message: String): Exception()

}
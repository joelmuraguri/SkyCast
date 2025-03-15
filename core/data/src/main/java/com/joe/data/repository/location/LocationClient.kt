package com.joe.data.repository.location

import com.joe.models.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun fetchCurrentLocation(): Flow<Location>
    class LocationException(message: String): Exception()

}
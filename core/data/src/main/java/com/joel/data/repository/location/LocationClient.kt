package com.joel.data.repository.location

import com.joel.models.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    suspend fun fetchCurrentLocation(): Flow<Location>
}
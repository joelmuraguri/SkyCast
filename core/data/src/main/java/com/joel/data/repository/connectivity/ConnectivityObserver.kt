package com.joel.data.repository.connectivity

import com.joel.models.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<ConnectivityStatus>
}
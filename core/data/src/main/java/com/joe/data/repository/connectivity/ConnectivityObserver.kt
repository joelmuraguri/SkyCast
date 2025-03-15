package com.joe.data.repository.connectivity

import com.joe.models.ConnectivityStatus
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<ConnectivityStatus>
}
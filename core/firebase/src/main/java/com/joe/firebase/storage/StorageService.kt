package com.joe.firebase.storage

import com.joe.models.ForecastInfo
import com.joe.models.User
import kotlinx.coroutines.flow.Flow

interface StorageService {

    val locations: Flow<List<ForecastInfo>>
    suspend fun getLocation(name: String): ForecastInfo?
    suspend fun save(location: ForecastInfo): String
    suspend fun saveUserInfo(user: User)
    suspend fun deleteUserInfo()
    suspend fun update(location: ForecastInfo)
    suspend fun delete(name: String)
    suspend fun deleteAllLocations()
}
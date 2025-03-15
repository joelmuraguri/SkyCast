package com.joe.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joe.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Upsert
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_forecast")
    fun getWeather(): Flow<WeatherEntity>

    @Query("DELETE FROM weather_forecast")
    suspend fun deleteWeather()
}

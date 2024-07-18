package com.joel.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.joel.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_forecast WHERE locationName = :locationName")
    fun getWeather(locationName: String): Flow<WeatherEntity?>

    @Query("DELETE FROM weather_forecast WHERE locationName = :locationName")
    suspend fun deleteWeather(locationName: String)
}

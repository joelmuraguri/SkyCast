package com.joel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joel.database.dao.FavouriteDao
import com.joel.database.dao.ForecastDao
import com.joel.database.entity.FavouritePlace
import com.joel.database.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, FavouritePlace::class],
    version = 1, exportSchema = false
)
abstract class SkyCastDatabase : RoomDatabase() {
    abstract fun foreCastDao() : ForecastDao
    abstract fun favouriteDao() : FavouriteDao
}
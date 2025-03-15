package com.joe.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joe.database.dao.FavouriteDao
import com.joe.database.dao.ForecastDao
import com.joe.database.entity.FavouritePlace
import com.joe.database.entity.WeatherEntity

@Database(
    entities = [WeatherEntity::class, FavouritePlace::class],
    version = 1, exportSchema = false
)
abstract class SkyCastDatabase : RoomDatabase() {
    abstract fun foreCastDao() : ForecastDao
    abstract fun favouriteDao() : FavouriteDao
}
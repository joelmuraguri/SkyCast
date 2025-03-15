package com.joe.database.di

import android.app.Application
import androidx.room.Room
import com.joe.database.SkyCastDatabase
import com.joe.database.dao.FavouriteDao
import com.joe.database.dao.ForecastDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
    ): SkyCastDatabase {
        return Room
            .databaseBuilder(application, SkyCastDatabase::class.java, "SkyCast.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideForecastDao(appDatabase: SkyCastDatabase): ForecastDao {
        return appDatabase.foreCastDao()
    }

    @Provides
    @Singleton
    fun provideFavouritesDao(appDatabase: SkyCastDatabase): FavouriteDao {
        return appDatabase.favouriteDao()
    }
}
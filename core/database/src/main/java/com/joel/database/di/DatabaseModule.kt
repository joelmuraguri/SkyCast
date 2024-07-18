package com.joel.database.di

import android.app.Application
import androidx.room.Room
import com.joel.database.SkyCastDatabase
import com.joel.database.dao.FavouriteDao
import com.joel.database.dao.ForecastDao
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
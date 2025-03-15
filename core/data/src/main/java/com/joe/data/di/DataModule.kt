package com.joe.data.di

import com.joe.data.repository.connectivity.ConnectivityObserver
import com.joe.data.repository.connectivity.DefaultConnectivityObserver
import com.joe.data.repository.forecast.ForecastRepository
import com.joe.data.repository.forecast.ForecastRepositoryImpl
import com.joe.data.repository.location.LocationRepository
import com.joe.data.repository.location.LocationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class DataModule {

    @Binds
    abstract fun bindsConnectivityRepository(connectivityRepo: DefaultConnectivityObserver): ConnectivityObserver

    @Binds
    abstract fun bindsForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository

    @Binds
    abstract fun bindsLocationsRepository(locationRepositoryImpl: LocationRepositoryImpl): LocationRepository
}

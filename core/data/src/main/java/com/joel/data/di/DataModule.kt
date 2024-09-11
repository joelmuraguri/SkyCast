package com.joel.data.di

import com.joel.data.repository.connectivity.ConnectivityObserver
import com.joel.data.repository.connectivity.DefaultConnectivityObserver
import com.joel.data.repository.forecast.ForecastRepository
import com.joel.data.repository.forecast.ForecastRepositoryImpl
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
}

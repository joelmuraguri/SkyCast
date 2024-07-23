package com.joel.data.di

import android.content.Context
import com.joel.data.repository.connectivity.ConnectivityObserver
import com.joel.data.repository.connectivity.DefaultConnectivityObserver
import com.joel.data.repository.forecast.ForecastRepository
import com.joel.data.repository.forecast.ForecastRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
//    @Provides
//    fun providesContext(
//        @ApplicationContext context: Context,
//    ): Context = context

    @Binds
    fun bindsConnectivityRepository(connectivityRepo: DefaultConnectivityObserver): ConnectivityObserver

    @Binds
    fun bindsForecastRepository(forecastRepositoryImpl: ForecastRepositoryImpl): ForecastRepository
}

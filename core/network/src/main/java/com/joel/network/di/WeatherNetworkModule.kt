package com.joel.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.joel.network.BuildConfig
import com.joel.network.WeatherJson
import com.joel.network.WeatherOkHttpClient
import com.joel.network.WeatherRetrofit
import com.joel.network.client.WeatherClient
import com.joel.network.service.WeatherService
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WeatherNetworkModule {

    @Provides
    @Singleton
    @WeatherJson
    fun provideWeatherJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @WeatherOkHttpClient
    fun provideWeatherOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    this.addNetworkInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
            }
            .build()
    }

    @Provides
    @Singleton
    @WeatherRetrofit
    fun provideForeCastRetrofit(
        @WeatherJson json: Json,
        @WeatherOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideForeCastService(@WeatherRetrofit retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherClient(service: WeatherService): WeatherClient {
        return WeatherClient(service)
    }
}

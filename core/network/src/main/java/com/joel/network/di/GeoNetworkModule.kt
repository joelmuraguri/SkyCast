package com.joel.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.joel.network.BuildConfig
import com.joel.network.GeoJson
import com.joel.network.GeoOkHttpClient
import com.joel.network.GeoRetrofit
import com.joel.network.client.GeoClient
import com.joel.network.service.GeoService
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
internal object GeoNetworkModule {

    @Provides
    @Singleton
    @GeoJson
    fun provideGeoJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @GeoOkHttpClient
    fun provideGeoOkHttpClient(): OkHttpClient {
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
    @GeoRetrofit
    fun provideGeoCodingRetrofit(
        @GeoJson json: Json,
        @GeoOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://geocoding-api.open-meteo.com/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGeoService(@GeoRetrofit retrofit: Retrofit): GeoService {
        return retrofit.create(GeoService::class.java)
    }

    @Provides
    @Singleton
    fun provideGeoClient(service: GeoService): GeoClient {
        return GeoClient(service)
    }
}

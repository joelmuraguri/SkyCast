package com.joe.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.joe.network.BuildConfig
import com.joe.network.ReverseGeoJson
import com.joe.network.ReverseGeoOkHttpClient
import com.joe.network.ReverseGeoRetrofit
import com.joe.network.client.ReverseGeoClient
import com.joe.network.service.ReverseGeoService
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
internal object ReverseGeoModule {

    @Provides
    @Singleton
    @ReverseGeoJson
    fun provideReverseGeoJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @ReverseGeoOkHttpClient
    fun provideReverseGeoOkHttpClient(): OkHttpClient {
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
    @ReverseGeoRetrofit
    fun provideReverseGeoCodingRetrofit(
        @ReverseGeoJson json: Json,
        @ReverseGeoOkHttpClient okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://geocode.maps.co/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideReverseGeoService(@ReverseGeoRetrofit retrofit: Retrofit): ReverseGeoService {
        return retrofit.create(ReverseGeoService::class.java)
    }

    @Provides
    @Singleton
    fun provideReverseGeoClient(service: ReverseGeoService): ReverseGeoClient {
        return ReverseGeoClient(service)
    }
}
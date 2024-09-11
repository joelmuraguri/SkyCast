package com.joel.network

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeoRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeoJson

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherJson

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeoOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherOkHttpClient

package com.joe.skycast.di

import com.joe.supabase.auth.AuthService
import com.joe.supabase.auth.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class DataModule {

    @Binds
    abstract fun bindsAuthService(service: AuthServiceImpl): AuthService

}
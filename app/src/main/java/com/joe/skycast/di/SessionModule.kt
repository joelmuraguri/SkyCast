package com.joe.skycast.di

import android.content.Context
import com.joe.supabase.auth.SessionService
import com.joe.supabase.auth.SessionServiceImpl
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    @Provides
    @Singleton
    fun provideSessionService(@ApplicationContext context: Context): SessionService {
        return SessionServiceImpl(context)
    }
}
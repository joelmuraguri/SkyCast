package com.joe.firebase.di

import com.joe.firebase.account.AccountService
import com.joe.firebase.account.AccountServiceImpl
import com.joe.firebase.account.AuthRepository
import com.joe.firebase.account.AuthRepositoryImpl
import com.joe.firebase.storage.StorageService
import com.joe.firebase.storage.StorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}
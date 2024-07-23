package com.joel.sync.di

import com.joel.sync.worker.SyncManager
import com.joel.sync.worker.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    @Binds
    internal abstract fun bindsSyncStatusMonitor(syncStatusMonitor: WorkManagerSyncManager): SyncManager
}

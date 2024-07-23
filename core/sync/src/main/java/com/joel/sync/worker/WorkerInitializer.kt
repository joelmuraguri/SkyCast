package com.joel.sync.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.WorkManager

object WorkerInitializer {
    fun initialize(context: Context) {
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            SyncWorker.startUpSyncWork(),
        )
    }
}

internal const val SYNC_WORK_NAME = "SyncWorkerName"

val SyncConstraints
    get() =
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

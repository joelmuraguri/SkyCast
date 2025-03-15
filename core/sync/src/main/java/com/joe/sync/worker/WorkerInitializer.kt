package com.joe.sync.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.WorkManager


object WorkerInitializer {
    fun initialize(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(SYNC_WORK_NAME, "------------------------> Access INITIALIZED function")
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                SyncWorker.startUpSyncWork()
            )
            Log.d(SYNC_WORK_NAME, "------------------------> SYNC WORKER IS INITIALIZED")
        } else {
            Log.d(SYNC_WORK_NAME, "------------------------> Permissions not granted, worker not initialized")
        }
    }
}


const val SYNC_WORK_NAME = "SyncWorkerName"

val SyncConstraints
    get() =
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

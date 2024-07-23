package com.joel.sync.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class WorkManagerSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) : SyncManager {

        override val isSyncing: Flow<Boolean> =
            WorkManager
                .getInstance(context)
                .getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
                .map(List<WorkInfo>::anyRunning)
                .conflate()

        override fun requestSync() {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                SYNC_WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                SyncWorker.startUpSyncWork(),
            )
        }
    }

private fun List<WorkInfo>.anyRunning() = any { it.state == State.RUNNING }

interface SyncManager {
    val isSyncing: Flow<Boolean>
    fun requestSync()
}

package com.joel.sync.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.joel.data.mappers.asEntity
import com.joel.data.repository.location.LocationClient
import com.joel.database.dao.ForecastDao
import com.joel.network.Dispatcher
import com.joel.network.SkyCastDispatchers
import com.joel.network.client.GeoClient
import com.joel.network.client.ReverseGeoClient
import com.joel.network.client.WeatherClient
import com.skydoves.sandwich.suspendOnSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Dispatcher(SkyCastDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val forecastDao: ForecastDao,
    private val client: WeatherClient,
    private val reverseGeoClient : ReverseGeoClient,
    private val locationClient: LocationClient,
    private val geoClient: GeoClient
) : CoroutineWorker(appContext, workerParams) {

    init {
        Log.d(SYNC_WORK_NAME, "------------------> SyncWorker created")
    }

    override suspend fun doWork(): Result = withContext(ioDispatcher) {
        Log.d(SYNC_WORK_NAME, "--------------------------> doWork: Work has started")

        /**
         * Checking for permission here is not needed at all but had to do it while debugging
         * Will remove it when more confident hahahahaha
         * **/
        val fineLocationGranted = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        Log.d(SYNC_WORK_NAME, "---------------> Fine location granted: $fineLocationGranted, Coarse location granted: $coarseLocationGranted")

        if (!fineLocationGranted || !coarseLocationGranted) {
            Log.e(SYNC_WORK_NAME, "-------------------> Missing location permission")
            return@withContext Result.failure()
        }

        try {
            Log.d(SYNC_WORK_NAME, "-------------------------------------------> Fetching current location")
            locationClient.fetchCurrentLocation().collect { location ->
                Log.d(SYNC_WORK_NAME, "-------------> Location fetched successfully: LAT: ${location.latitude}, LON: ${location.longitude}")
                val response = client.forecast(location.latitude, location.longitude)
                var locationName = "__:__"
                reverseGeoClient.getLocationAddress(location.latitude, location.longitude).suspendOnSuccess {
                    locationName  = data.address.suburb ?: data.address.county ?: "__:__"
                    Log.d(SYNC_WORK_NAME, "---------------------> LOCATION NAME : $locationName")
                }
                response.suspendOnSuccess {
                    Log.d(SYNC_WORK_NAME, "---------------------> Success: $data")
                    forecastDao.deleteWeather()
                    forecastDao.insertWeather(data.asEntity(System.currentTimeMillis(), locationName))
                }
                geoClient.locationSearch("").suspendOnSuccess {
                   val a = data.results
                }
            }
            Log.d(SYNC_WORK_NAME, "--------------------------------> Work completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e(SYNC_WORK_NAME, "-----------------------------------> Error: ${e.message}", e)
            Result.failure()
        }
    }

    companion object {
        fun startUpSyncWork() =
            PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS)
                .setConstraints(SyncConstraints)
                .build()
    }
}


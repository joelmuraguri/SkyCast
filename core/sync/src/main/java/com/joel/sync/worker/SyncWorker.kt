package com.joel.sync.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.joel.data.mappers.asEntity
import com.joel.data.repository.location.LocationClient
import com.joel.database.dao.ForecastDao
import com.joel.network.Dispatcher
import com.joel.network.SkyCastDispatchers
import com.joel.network.client.WeatherClient
import com.joel.network.models.ForecastResponse
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
        private val locationClient: LocationClient
) : CoroutineWorker(appContext, workerParams) {

        override suspend fun doWork(): Result = withContext(ioDispatcher) {
            try {
                locationClient.fetchCurrentLocation().collect { location ->
                    val response = client.forecast(location.latitude, location.longitude)
                    response.suspendOnSuccess {
                        forecastDao.insertWeather(data.asEntity())
                    }
                }

                // Handle new favorite locations
//                val favoriteLocations = forecastDao.getFavoriteLocations()
//                favoriteLocations.forEach { favoriteLocation ->
//                    val favoriteWeatherData = client.getWeatherData(favoriteLocation.latitude, favoriteLocation.longitude)
//                    forecastDao.insertWeatherData(favoriteWeatherData)
//                }

                Result.success()
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }

        }

        companion object {
            fun startUpSyncWork() =
                PeriodicWorkRequestBuilder<SyncWorker>(3, TimeUnit.HOURS)
                    .setConstraints(SyncConstraints)
                    .build()
        }
}

package com.joel.data.repository.forecast

import androidx.annotation.WorkerThread
import com.joel.data.mappers.asDomain
import com.joel.database.dao.ForecastDao
import com.joel.models.WeatherDomain
import com.joel.network.Dispatcher
import com.joel.network.SkyCastDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val forecastDao: ForecastDao,
    @Dispatcher(SkyCastDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : ForecastRepository {

    @WorkerThread
    override fun fetchWeatherForecast(
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    )  : Flow<WeatherDomain> = flow{
        try {
            val weatherEntity = withContext(ioDispatcher) {
                forecastDao.getWeather()
            }
            emit(weatherEntity.asDomain())
            onComplete()
        } catch (e: Exception) {
            onError(e.message)
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}
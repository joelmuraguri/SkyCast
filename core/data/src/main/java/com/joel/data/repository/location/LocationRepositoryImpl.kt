package com.joel.data.repository.location

import android.util.Log
import androidx.annotation.WorkerThread
import com.joel.data.mappers.asDomain
import com.joel.data.mappers.asEntity
import com.joel.models.Place
import com.joel.models.WeatherDomain
import com.joel.network.Dispatcher
import com.joel.network.SkyCastDispatchers
import com.joel.network.client.GeoClient
import com.joel.network.client.ReverseGeoClient
import com.joel.network.client.WeatherClient
import com.joel.network.models.ForecastResponse
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class LocationRepositoryImpl @Inject constructor(
    private val client: GeoClient,
    private val weatherClient: WeatherClient,
    private val geoClient: ReverseGeoClient,
    @Dispatcher(SkyCastDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : LocationRepository {

    @WorkerThread
    override fun locationsSearch(
        query: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        try {
            val response = client.locationSearch(query)
            response.suspendOnSuccess {
                val places = data.results.map { it.asEntity().asDomain() }
                if (places.isEmpty()) {
                    onError("No places found")
                    emit(emptyList<Place>())
                } else {
                    emit(places)
                }
            }.suspendOnError {
                Log.e("LocationRepository", "API response error: ${this.payload}")
                onError((this.payload.toString() ?: "Unknown API error"))
            }.suspendOnException {
                Log.e("LocationRepository", "Exception: ${throwable.message}")
                onError("Exception occurred during API request: ${throwable.message}")
            }
            onComplete()
        } catch (e: Exception) {
            Log.e("LocationRepository", "Exception: ${e.message}")
            onError(e.message)
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)

    override fun fetchLocationForecast(
        latitude: Double, longitude: Double,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<WeatherDomain> = flow {
        try {
            weatherClient.forecast(latitude, longitude).suspendOnSuccess {
                var locationName = "__:__"
                geoClient.getLocationAddress(latitude, longitude).suspendOnSuccess {
                    locationName = data.address.suburb ?: data.address.county ?: "__:__"
                }
                val weatherDomain = mapResponseToDomain(
                    response = data,
                    timeStamp = System.currentTimeMillis(),
                    locationName = locationName
                )
                emit(weatherDomain)
                Log.d("LOCATION FORECAST", "------------------> $weatherDomain")
            }.suspendOnError {
                onError(this.payload?.toString() ?: "Unknown error")
            }.suspendOnException {
                onError(throwable.message)
            }
        } catch (e: Exception) {
            onError(e.message)
        } finally {
            onComplete()
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)

    private fun mapResponseToDomain(response: ForecastResponse, timeStamp : Long, locationName : String): WeatherDomain {
        return response.asEntity(timestamp = timeStamp, locationName = locationName).asDomain()
    }
}

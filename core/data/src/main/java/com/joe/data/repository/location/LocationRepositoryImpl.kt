package com.joe.data.repository.location

import android.util.Log
import androidx.annotation.WorkerThread
import com.joe.data.mappers.asDomain
import com.joe.data.mappers.asEntity
import com.joe.models.Place
import com.joe.models.WeatherDomain
import com.joe.network.Dispatcher
import com.joe.network.SkyCastDispatchers
import com.joe.network.client.GeoClient
import com.joe.network.client.ReverseGeoClient
import com.joe.network.client.WeatherClient
import com.joe.network.models.ForecastResponse
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
                Log.d(LOCATION_REPOSITORY, "---------------------> LOCATION NAME : $locationName")
                Log.d(LOCATION_REPOSITORY, "---------------------> LAT : $latitude")
                Log.d(LOCATION_REPOSITORY, "---------------------> LON : $longitude")

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

    companion object{
        const val LOCATION_REPOSITORY = "Location Repository"
    }

}

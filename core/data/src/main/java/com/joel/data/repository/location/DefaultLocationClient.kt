package com.joel.data.repository.location

import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.joel.models.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DefaultLocationClient(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val currentLocationRequest: LocationRequest
) : LocationClient {

    override suspend fun fetchCurrentLocation(): Flow<Location> {
        return callbackFlow {
            val callback = object : LocationCallback(){
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    try {
                        trySend(
                            Location(
                                longitude = result.lastLocation?.longitude ?: 0.0,
                                latitude = result.lastLocation?.latitude ?: 0.0
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("Error", e.message.toString())
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                currentLocationRequest,
                callback,
                Looper.getMainLooper()
            )
                .addOnFailureListener { e ->
                    close(e)
                }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(callback)
            }
        }
    }
}
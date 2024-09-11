package com.joel.data.repository.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.joel.models.Location
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class DefaultLocationClient @Inject constructor(
    private val context: Context,
    private val client: FusedLocationProviderClient,
    private val currentLocationRequest: LocationRequest
): LocationClient {

    @SuppressLint("MissingPermission")
    override fun fetchCurrentLocation(): Flow<Location> {
        return callbackFlow {
            Log.d("LOCATION CLIENT", "fetchCurrentLocation: Starting location request")

            if (!context.hasLocationPermission()) {
                Log.e("LOCATION CLIENT", "No location permission")
                close(LocationClient.LocationException("Missing location permission"))
                return@callbackFlow
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            Log.d("LOCATION CLIENT", "GPS Enabled: $isGpsEnabled, Network Enabled: $isNetworkEnabled")

            if (!isGpsEnabled && !isNetworkEnabled) {
                Log.e("LOCATION CLIENT", "GPS and Network providers are disabled")
                close(LocationClient.LocationException("GPS is disabled"))
                return@callbackFlow
            }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    Log.d("LOCATION CLIENT", "onLocationResult called")
                    result.lastLocation?.let {
                        Log.d("LOCATION CLIENT", "Location received: LAT: ${it.latitude}, LON: ${it.longitude}")
                        trySend(
                            Location(
                                longitude = it.longitude,
                                latitude = it.latitude
                            )
                        ).isSuccess
                    } ?: run {
                        Log.e("LOCATION CLIENT", "No location received")
                        close(LocationClient.LocationException("No location received"))
                    }
                }
            }


            client.requestLocationUpdates(currentLocationRequest, locationCallback, Looper.getMainLooper())

            awaitClose {
                client.removeLocationUpdates(locationCallback)
                Log.d("LOCATION CLIENT", "Location updates removed")
            }
        }
    }
}

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
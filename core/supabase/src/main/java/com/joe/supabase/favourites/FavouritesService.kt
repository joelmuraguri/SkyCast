package com.joe.supabase.favourites

import android.accounts.AuthenticatorException
import android.util.Log
import com.joe.models.ForecastInfo
import com.joe.supabase.auth.AuthResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.http.parsing.ParseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface FavouritesService {
    suspend fun createFavouriteLocation(forecastInfo: ForecastInfo, cityName: String,timeZone : String): Flow<AuthResponse>
    suspend fun getFavouriteLocations():  Flow<List<FavouriteLocation>>
    suspend fun deleteFavouriteLocation(id: String) : Flow<Boolean>
    suspend fun updateFavouriteLocations(favouriteLocation: FavouriteLocation) : Flow<Boolean>
}

class FavouriteServiceImpl @Inject constructor(
    private val supabase : SupabaseClient,
    private val postgrest: Postgrest
) : FavouritesService{

    private val userId = supabase.auth.currentUserOrNull()?.id

    override suspend fun createFavouriteLocation(
        forecastInfo: ForecastInfo,
        cityName : String,
        timeZone : String
    ): Flow<AuthResponse> = flow {
        try {
            val favourite = forecastInfo.asSupabaseEntity(
                id = java.util.UUID.randomUUID().toString(),
                userId = userId!!,
                cityName = cityName,
                timeZone = timeZone
//                lastUpdated = java.time.Instant.now().toString()
            )
            Log.d(FAVOURITE_REPOSITORY, "-----------------------> ${favourite.toString()}")
            postgrest.from("favorite_locations").insert(favourite)
            emit(AuthResponse.Success)
        } catch (e: ParseException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: RestException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: AuthenticatorException) {
            emit(AuthResponse.Error(e.localizedMessage))
        }catch (e: Exception) {
            emit(AuthResponse.Error(e.localizedMessage))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getFavouriteLocations(): Flow<List<FavouriteLocation>> = flow {
        try {
            val result = postgrest.from("favorite_locations")
                .select()
                .decodeList<FavouriteLocation>()

            emit(result)
        } catch (e: ParseException) {
            Log.e("FavouriteLocations", "Parse error: ${e.localizedMessage}")
            emit(emptyList())
        } catch (e: RestException) {
            Log.e("FavouriteLocations", "Rest error: ${e.localizedMessage}")
            emit(emptyList())
        } catch (e: AuthenticatorException) {
            Log.e("FavouriteLocations", "Authentication error: ${e.localizedMessage}")
            emit(emptyList())
        } catch (e: Exception) {
            Log.e("FavouriteLocations", "Unexpected error: ${e.localizedMessage}")
            emit(emptyList())
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun deleteFavouriteLocation(id: String) : Flow<Boolean> = flow {
        try {
            postgrest.from("favorite_locations").delete {
                filter {
                    eq("id", id)
                }
            }
            emit(true)
        } catch (e: ParseException) {
            Log.e("FavouriteLocations", "Parse error: ${e.localizedMessage}")
            emit(false)
        } catch (e: RestException) {
            Log.e("FavouriteLocations", "Rest error: ${e.localizedMessage}")
            emit(false)
        } catch (e: AuthenticatorException) {
            Log.e("FavouriteLocations", "Authentication error: ${e.localizedMessage}")
            emit(false)
        } catch (e: Exception) {
            Log.e("FavouriteLocations", "Unexpected error: ${e.localizedMessage}")
            emit(false)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateFavouriteLocations(favouriteLocation: FavouriteLocation): Flow<Boolean> {
        TODO("Not yet implemented")
    }


    companion object{
        const val FAVOURITE_REPOSITORY = "Favourite Repository"
    }


}

fun ForecastInfo.asSupabaseEntity(
    id: String,
    userId: String,
    cityName: String,
    timeZone : String
): FavouriteLocation {

    val now = ZonedDateTime.now(ZoneId.of(timeZone))
    val hourString = now.hour.toString().padStart(2, '0')

    val currentHour = hourlyForecast.find { hf ->
        hf.time.contains("T$hourString")
    }

    val daily = dailyForeCast.firstOrNull()

    return FavouriteLocation(
        id = id,
        userId = userId,
        locationName = cityName,
        latitude = location.latitude.toFloat(),
        longitude = location.longitude.toFloat(),
        weatherDescription = currentHour?.weather?.weatherDesc ?: "Unknown",
        currentTemperature = currentHour?.temp?.toIntOrNull() ?: 0,
        highTemp = daily?.highTemp ?: 0,
        lowTemp = daily?.lowTemp ?: 0,
        time = now.format(DateTimeFormatter.ofPattern("HH:mm")), // Display-friendly time
        weatherCode = currentHour?.weather?.code ?: 0
    )
}


//fun ForecastInfo.asSupabaseEntity(
//    id: String, // generate with UUID.randomUUID().toString()
//    userId: String, // fetch from Supabase session or passed in,
//    cityName: String
////    lastUpdated: String // ISO 8601 format, e.g., Instant.now().toString()
//): FavouriteLocation {
//
//    val currentHour = hourlyForecast.firstOrNull() // Or use time matching logic if needed
//    val daily = dailyForeCast.firstOrNull()
//
//    return FavouriteLocation(
//        id = id,
//        userId = userId,
//        locationName = cityName,
//        latitude = location.latitude.toFloat(),
//        longitude = location.longitude.toFloat(),
//        weatherDescription = currentHour?.weather?.weatherDesc ?: "Unknown",
//        currentTemperature = hourlyForecast.,
//        highTemp = daily?.highTemp ?: 0,
//        lowTemp = daily?.lowTemp ?: 0,
////        iconCode = (currentHour?.weather?.iconRes ?: "default").toString(), // change if this is not a String
////        lastUpdated = lastUpdated,
//        time = currentHour?.time ?: "00:00",
//        icon = currentHour?.weather?.iconRes ?: 0 // assuming iconResId is an Int
//    )
//}

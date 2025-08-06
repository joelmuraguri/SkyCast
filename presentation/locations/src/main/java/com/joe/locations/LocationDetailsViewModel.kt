package com.joe.locations

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.data.mappers.asPresentation
import com.joe.data.repository.location.LocationRepository
import com.joe.models.ForecastInfo
import com.joe.models.Place
import com.joe.supabase.auth.AuthResponse
import com.joe.supabase.favourites.FavouritesService
import com.muraguri.design.SkyCastEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val favouritesService: FavouritesService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

//    private val latitude: Double = savedStateHandle["latitude"] ?: 0.0
//    private val longitude: Double = savedStateHandle["longitude"] ?: 0.0

    private val place: Place = Json.decodeFromString(
        savedStateHandle.get<String>("place")
            ?: throw IllegalArgumentException("Missing navigation argument: place")
    )   ?: throw IllegalArgumentException("Missing navigation argument: place")

    private val latitude = place.location.latitude
    private val longitude = place.location.longitude


    private val _uiEvents = Channel<SkyCastEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()


    val forecastInfoState: StateFlow<DetailsUiState> =
        locationRepository.fetchLocationForecast(
            latitude = latitude,
            longitude = longitude,
            onError = { DetailsUiState.Error(it) },
            onComplete = { DetailsUiState.Idle },
            onStart = { DetailsUiState.Loading }
        ).map {
            Log.d("PRESENTATION DATA", "--------> ${it.asPresentation()}")
            DetailsUiState.Success(it.asPresentation())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailsUiState.Loading,
        )



    fun onEvents(events: DetailsEvents) {
        when (events) {
            is DetailsEvents.OnAdd -> {
                viewModelScope.launch {
                    val state = forecastInfoState.value
                    if (state is DetailsUiState.Success) {
                        val forecastInfo = state.forecastInfo
                        favouritesService.createFavouriteLocation(forecastInfo, cityName = events.cityName, timeZone = events.timeZone)
                            .collect { response ->
                                when (response) {
                                    is AuthResponse.Success -> {
                                        Log.d("FAVOURITE", "----------------> Successfully saved to Supabase")
                                        _uiEvents.send(SkyCastEvents.ShowSnackbar("Successfully saved to Supabase"))
                                    }
                                    is AuthResponse.Error -> {
                                        Log.e("FAVOURITE", "---------------> Error saving: ${response.message}")
                                        _uiEvents.send(SkyCastEvents.ShowSnackbar("Error saving: ${response.message}"))
                                    }
                                }
                            }
                    } else {
                        Log.w("FAVOURITE", "ForecastInfo not available yet.")
                    }
                    _uiEvents.send(SkyCastEvents.PopBackStack)
                }
            }
        }
    }
}


sealed class DetailsEvents{
    data class OnAdd(
        val cityName : String,
        val timeZone : String
    ) : DetailsEvents()
}

@Stable
sealed interface DetailsUiState {
    data object Idle : DetailsUiState

    data object Loading : DetailsUiState

    data class Success(val forecastInfo : ForecastInfo) : DetailsUiState

    data class Error(val message: String?) : DetailsUiState
}
package com.joel.locations

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.data.mappers.asPresentation
import com.joel.data.repository.location.LocationRepository
import com.joel.home.vm.HomeUiState
import com.joel.models.ForecastInfo
import com.joel.models.WeatherDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LocationDetailsViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val latitude: Double = savedStateHandle["latitude"] ?: 0.0
    private val longitude: Double = savedStateHandle["longitude"] ?: 0.0

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
            DetailsEvents.onAdd -> {
                // Handle add event
            }
        }
    }
}


sealed class DetailsEvents{
    data object onAdd : DetailsEvents()
}

@Stable
sealed interface DetailsUiState {
    data object Idle : DetailsUiState

    data object Loading : DetailsUiState

    data class Success(val forecastInfo : ForecastInfo) : DetailsUiState

    data class Error(val message: String?) : DetailsUiState
}
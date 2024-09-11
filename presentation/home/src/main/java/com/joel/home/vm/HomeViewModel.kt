package com.joel.home.vm

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.data.mappers.asPresentation
import com.joel.data.repository.forecast.ForecastRepository
import com.joel.models.ForecastInfo
import com.joel.sync.worker.SyncManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    forecastRepository : ForecastRepository,
): ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state : State<HomeScreenState> = _state


    val forecastInfoState : StateFlow<HomeUiState> =
        forecastRepository.fetchWeatherForecast(
            onError = { HomeUiState.Error(it)},
            onComplete = { HomeUiState.Idle},
            onStart = { HomeUiState.Loading }
        ).map {
            HomeUiState.Success(it.asPresentation())
        }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeUiState.Loading,
            )


    fun onEvents(events: HomeEvents){
        when(events){
            is HomeEvents.ShowMoreInfoClick -> {
                _state.value = _state.value.copy(
                    showMore = true
                )
            }
        }
    }

}

@Stable
sealed interface HomeUiState {

    data object Idle : HomeUiState

    data object Loading : HomeUiState

    data class Success(val forecastInfo : ForecastInfo) : HomeUiState

    data class Error(val message: String?) : HomeUiState
}

sealed class HomeEvents{
    data class ShowMoreInfoClick(val showMore : Boolean) : HomeEvents()
}

data class HomeScreenState(
    val showMore: Boolean = false
)
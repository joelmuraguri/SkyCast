package com.joe.locations

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joe.data.repository.location.LocationRepository
import com.joe.models.Place
import com.joe.models.WeatherDomain
import com.joe.supabase.auth.AuthResponse
import com.joe.supabase.auth.AuthService
import com.joe.supabase.favourites.FavouriteLocation
import com.joe.supabase.favourites.FavouritesService
import com.muraguri.design.SkyCastEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val authService : AuthService,
    private val favouritesService: FavouritesService
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEvents = Channel<SkyCastEvents>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    private val _favourites = MutableStateFlow<List<FavouriteLocation>>(emptyList())
    val favourites: StateFlow<List<FavouriteLocation>> = _favourites.asStateFlow()

    private val _favouritesLoading = MutableStateFlow(false)
    val favouritesLoading: StateFlow<Boolean> = _favouritesLoading.asStateFlow()

    private val _favourites2 = MutableStateFlow<List<FavouriteLocationWithLiveWeather>>(emptyList())
    val favourites2: StateFlow<List<FavouriteLocationWithLiveWeather>> = _favourites2.asStateFlow()



    init {
        _isAuthenticated.value = authService.isUserAuthenticated()

        Log.d(LOCATION_VIEWMODEL_TAG, "------------------> AUTH STATUS : ${_isAuthenticated.value}")
        Log.d(LOCATION_VIEWMODEL_TAG, "------------------> AUTH STATUS : ${authService.currentUser}")
        Log.d(LOCATION_VIEWMODEL_TAG, "------------------> AUTH STATUS : ${authService.currentUserId}")

        if (_query.value.isBlank()) {
            loadFavourites()
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            _favouritesLoading.value = true
            favouritesService.getFavouriteLocations()
                .catch { e ->
                    _uiState.value = SearchUiState.Error("Failed to load favourites: ${e.localizedMessage}")
                }
                .collect { favourites ->
                    _favourites.value = favourites
                    _favouritesLoading.value = false
                }
        }
    }


    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isNotBlank()) {
            performSearch(newQuery)
        } else {
            loadFavourites()
            _uiState.value = SearchUiState.Idle // Remain idle
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            try {
                _uiState.value = SearchUiState.Loading

                locationRepository.locationsSearch(
                    query = query,
                    onStart = { /* Removed redundant loading state */ },
                    onComplete = { },
                    onError = { error ->
                        _uiState.value = SearchUiState.Error(error ?: "Unknown error")
                    }
                ).collect { places ->
                    _uiState.value = if (places.isNotEmpty()) {
                        SearchUiState.Success(places)
                    } else {
                        SearchUiState.Idle
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(e.localizedMessage ?: "Unexpected error")
            }
        }
    }

    private fun loadFavourites2() {
        viewModelScope.launch {
            _favouritesLoading.value = true
            favouritesService.getFavouriteLocations()
                .catch { e ->
                    _uiState.value = SearchUiState.Error("Failed to load favourites: ${e.localizedMessage}")
                    _favouritesLoading.value = false
                }
                .collect { favs ->
                    val enriched = favs.map { fav ->
                        async {
                            val weather = fetchLiveWeatherForFavourite(fav)
                            FavouriteLocationWithLiveWeather(fav, weather)
                        }
                    }.awaitAll()

                    _favourites2.value = enriched
                    _favouritesLoading.value = false
                }
        }
    }


    fun googleSignIn(activity : Activity){
        viewModelScope.launch{
            authService.googleSignIn(activity).collect{
                when(it){
                    is AuthResponse.Error -> {
                        Log.e(LOCATION_VIEWMODEL_TAG, "Google Sign In Failed: ${it.message}")
                        _uiEvents.send(SkyCastEvents.ShowSnackbar("Google Sign In Failed: ${it.message}"))
                    }
                    AuthResponse.Success -> {
                        _isAuthenticated.value = true
                        loadFavourites() // this will help trigger recomposition due to favourites state
                        _uiState.value = SearchUiState.Idle // trigger screen recomposition
                        _uiEvents.send(SkyCastEvents.ShowSnackbar("Sign In Successful!"))
                    }
                }
            }
        }
    }

    private suspend fun fetchLiveWeatherForFavourite(fav: FavouriteLocation): WeatherDomain? {
        return try {
            var weather: WeatherDomain? = null
            locationRepository.fetchLocationForecast(
                latitude = fav.latitude.toDouble(),
                longitude = fav.longitude.toDouble(),
                onStart = {},
                onComplete = {},
                onError = {}
            ).collect {
                weather = it
            }
            weather
        } catch (e: Exception) {
            null // fallback if something fails
        }
    }


    companion object{
        const val LOCATION_VIEWMODEL_TAG = "LocationViewModel"
    }
}

sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val results: List<Place>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

data class FavouriteLocationWithLiveWeather(
    val favourite: FavouriteLocation,
    val liveWeather: WeatherDomain? = null
)

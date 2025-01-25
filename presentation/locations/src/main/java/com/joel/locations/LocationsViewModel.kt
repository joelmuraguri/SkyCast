package com.joel.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joel.data.repository.location.LocationRepository
import com.joel.models.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
        if (newQuery.isNotBlank()) {
            performSearch(newQuery)
        } else {
            _uiState.value = SearchUiState.Idle
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading

            locationRepository.locationsSearch(
                query = query,
                onStart = { _uiState.value = SearchUiState.Loading },
                onComplete = { },
                onError = { error ->
                    _uiState.value = SearchUiState.Error(error ?: "Unknown error")
                }
            ).collect { places ->
                if (places.isNotEmpty()) {
                    _uiState.value = SearchUiState.Success(places)
                } else {
                    _uiState.value = SearchUiState.Error("No places found.")
                }
            }
        }
    }
}


sealed class SearchUiState {
    data object Idle : SearchUiState()
    data object Loading : SearchUiState()
    data class Success(val results: List<Place>) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

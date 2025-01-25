package com.joel.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joel.models.GridItem
import com.joel.models.GridItemType
import com.joel.home.contents.HomeAppBar
import com.joel.home.contents.HourlyForecast
import com.joel.home.contents.LocationWeatherDetails
import com.joel.home.vm.HomeEvents
import com.joel.home.vm.HomeUiState
import com.joel.home.vm.HomeViewModel
import com.joel.home.vm.TimeViewModel

const val design= "https://www.behance.net/gallery/169457873/Fairy-weather-forecast-mobile-web-progressive-app?tracking_source=search_projects|weather+app&l=60"


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: TimeViewModel = viewModel()
) {
    val state = homeViewModel.state.value
    val currentTime by viewModel.currentTime
    val forecastInfoState by homeViewModel.forecastInfoState.collectAsStateWithLifecycle()

    Log.d("----------> HomeScreen", "Current showMore state: ${state.showMore}")


    when (forecastInfoState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .background(Color(0xFF142036))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeUiState.Success -> {
            val forecastInfo = (forecastInfoState as HomeUiState.Success).forecastInfo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF142036))
            ){
                Box(
                    contentAlignment = Alignment.TopStart,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HomeAppBar(
                        text = forecastInfo.name,
                        hideDetails = state.showMore,
                        showMoreClick = {
                            Log.d("----------> HomeScreen", "Calling ShowMoreClick $it")
                            homeViewModel.onEvents(HomeEvents.ShowMoreInfoClick(it))
                        },
                        showLessClick = {
                            Log.d("----------> HomeScreen", "Calling ShowLessClick $it")
                            homeViewModel.onEvents(HomeEvents.ShowLessInfoClick(it))
                        }
                    )
                }
                LazyColumn {
                    val gridItems = listOf(
                        GridItem(GridItemType.Temperature, Pair("30°", "15°")),
                        GridItem(GridItemType.UVIndex, 8),
                        GridItem(GridItemType.Humidity, "70%"),
                        GridItem(GridItemType.Wind, "15 km/h"),
                        GridItem(GridItemType.Precipitation, "5 mm"),
                        GridItem(GridItemType.Pressure, "1013 hPa")
                    )
                    item {
                        LocationWeatherDetails(
                            forecastInfo = forecastInfo,
                            showMoreInfo = state.showMore,
                            items = gridItems,currentTime
                        )
                    }
                    item {
                        HourlyForecast(hourlyForecastItems = forecastInfo.hourlyForecast, currentTime)
                    }
                }
            }
        }
        is HomeUiState.Error -> {
            val errorMessage = (forecastInfoState as HomeUiState.Error).message
            Box(
                modifier = Modifier
                    .background(Color(0xFF142036))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = errorMessage!!)
            }
        }
        is HomeUiState.Idle -> {
            // Handle idle state
        }
    }
}




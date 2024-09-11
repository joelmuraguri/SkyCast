package com.joel.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joel.home.contents.GridItem
import com.joel.home.contents.GridItemType
import com.joel.home.contents.HomeAppBar
import com.joel.home.contents.HourlyForecast
import com.joel.home.contents.LocationWeatherDetails
import com.joel.home.vm.HomeEvents
import com.joel.home.vm.HomeUiState
import com.joel.home.vm.HomeViewModel

const val design= "https://www.behance.net/gallery/169457873/Fairy-weather-forecast-mobile-web-progressive-app?tracking_source=search_projects|weather+app&l=60"

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
){

    val forecastState by homeViewModel.forecastInfoState.collectAsStateWithLifecycle()
//    val isSyncing by homeViewModel.isSyncing.collectAsStateWithLifecycle()

    val state = homeViewModel.state.value

    when(forecastState){
        is HomeUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = (forecastState as HomeUiState.Error).message!!)
            }
        }
        HomeUiState.Idle -> TODO()
        HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is HomeUiState.Success -> {
            Scaffold (
                topBar = {
                    HomeAppBar(text = (forecastState as HomeUiState.Success).forecastInfo.name, onClick = { homeViewModel.onEvents(HomeEvents.ShowMoreInfoClick(it)) }, hideDetails = state.showMore)
                }
            ){
                Box(
                    modifier = Modifier
                        .padding(it)
                ){
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
//                            if (isSyncing) {
//                                Box{
//                                    Text("Syncing data...")
//                                }
//                            }
                            LocationWeatherDetails(
                                forecastInfo = (forecastState as HomeUiState.Success).forecastInfo,
                                showMoreInfo = state.showMore,
                                items = gridItems
                            )
                        }
//                        item {
//                            LocationWeatherDetails(
//                                forecastInfo = (forecastState as HomeUiState.Success).forecastInfo,
//                                showMoreInfo = state.showMore, items = gridItems)
//                        }
                        item {
                            HourlyForecast(hourlyForecastItems = (forecastState as HomeUiState.Success).forecastInfo.hourlyForecast)
                        }
                    }
                }
            }

        }
    }
}


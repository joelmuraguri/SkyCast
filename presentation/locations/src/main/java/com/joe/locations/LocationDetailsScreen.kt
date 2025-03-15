package com.joe.locations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joe.home.contents.HourlyForecast
import com.joe.home.contents.LocationWeatherDetails
import com.joe.home.vm.TimeViewModel
import com.joe.models.GridItem
import com.joe.models.GridItemType
import com.joe.models.Place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailsScreen(
    onCancel : () -> Unit,
    onAdd : () -> Unit,
    place: Place,
    locationDetailsViewModel : LocationDetailsViewModel = hiltViewModel(),
    viewModel: TimeViewModel = viewModel()
) {

    val currentTime by viewModel.currentTime
    val forecastInfoState by locationDetailsViewModel.forecastInfoState.collectAsStateWithLifecycle()


    when (forecastInfoState) {
        is DetailsUiState.Loading -> {
            Box(
                modifier = Modifier
                    .background(Color(0xFF142036))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailsUiState.Success -> {
            val forecastInfo = (forecastInfoState as DetailsUiState.Success).forecastInfo
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
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            TextButton(
                                onClick = onCancel
                            ) {
                                Text(
                                    "Cancel",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        },
                        title = {
                            Text(
                                place.name,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        actions = {
                            TextButton(
                                onClick = onAdd
                            ) {
                                Text(
                                    "Add", color = Color(0xFFFEB800),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF142036)
                        ),
                        modifier = Modifier
                            .height(65.dp)
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
                            showMoreInfo = true,
                            items = gridItems,currentTime
                        )
                    }
                    item {
                        HourlyForecast(hourlyForecastItems = forecastInfo.hourlyForecast, currentTime)
                    }
                }
            }
        }
        is DetailsUiState.Error -> {
            val errorMessage = (forecastInfoState as DetailsUiState.Error).message
            Box(
                modifier = Modifier
                    .background(Color(0xFF142036))
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = errorMessage!!)
            }
        }
        is DetailsUiState.Idle -> {
            // Handle idle state
        }
    }
}

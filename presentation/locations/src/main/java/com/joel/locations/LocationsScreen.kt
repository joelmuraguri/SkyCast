package com.joel.locations

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joel.locations.components.EmptyLocationsState
import com.joel.locations.components.SearchAppBar
import com.joel.models.Place

@Composable
fun LocationsScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToDetails: (Place) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        SearchAppBar(
            isEmpty = false,
            value = query,
            onValueChange = {
                viewModel.updateQuery(it)
            }
        )
        Log.d("LocationsScreen", "---------------> UI State: $uiState")

        when (uiState) {
            is SearchUiState.Idle -> EmptyLocationsState()
            is SearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF142036))
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
            is SearchUiState.Success -> {
                val results = (uiState as SearchUiState.Success).results
                ResultsList(results = results, query = query, onItemClick = { place ->
                    onNavigateToDetails(place)
                })            }
            is SearchUiState.Error -> {
                val errorMessage = (uiState as SearchUiState.Error).message
                ErrorContent(message = errorMessage)
            }
        }
    }
}


@Composable
fun ResultsList(
    query: String,
    results: List<Place>,
    onItemClick: (Place) -> Unit
) {
    Log.d("ResultsList", "Results: $results")
    if (results.isEmpty()) {
        Text(
            text = "No results found.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(results) { place ->
                SearchResultItem(query = query, place = place, onClick = { onItemClick(place) })
            }
        }
    }
}


@Composable
fun SearchResultItem(query: String, place: Place, onClick: () -> Unit) {
    val highlightedText = buildAnnotatedString {
        val text = place.name
        val queryStartIndex = text.indexOf(query, ignoreCase = true)

        if (queryStartIndex != -1) {
            append(text.substring(0, queryStartIndex))
            withStyle(style = SpanStyle(color = Color(0xFFFEB800))) {
                append(text.substring(queryStartIndex, queryStartIndex + query.length))
            }
            append(text.substring(queryStartIndex + query.length))
        } else {
            append(text)
        }
        append(", ")
    }

    Row(
        modifier = Modifier
        .clickable { onClick() }
        .padding(vertical = 8.dp)) {
        Text(
            text = highlightedText,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = place.country,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF6C788E)
        )
    }
}


@Composable
fun ErrorContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oops! Something went wrong.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

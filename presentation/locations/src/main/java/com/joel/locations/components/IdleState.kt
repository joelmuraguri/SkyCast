package com.joel.locations.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyLocationsState() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = null,
            modifier = Modifier
                .padding(10.dp)
                .size(50.dp)
        )
        Text(
            text = "Add a location", fontSize = 24.sp, color = Color.White,
            modifier = Modifier
                .padding(10.dp)
        )
        Text(
            text = "Use the search bar to find and then add the location you need",
            color = Color(0xFF6C788E),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun IdleStatePreview() {
    MaterialTheme {
        EmptyLocationsState()
    }
}
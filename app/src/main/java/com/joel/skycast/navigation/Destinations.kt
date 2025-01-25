package com.joel.skycast.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.joel.models.Place
import kotlinx.serialization.Serializable


sealed class Destinations {
    @Serializable
    data object Home : Destinations()
    @Serializable
    data object Locations : Destinations()
    @Serializable
    data object Settings : Destinations()
    @Serializable
    data class LocationDetails(val place : Place) : Destinations()
}

enum class BottomNavigation(val label: String, val icon: ImageVector, val route: Destinations) {
    HOME("Home", Icons.Filled.Home, Destinations.Home),
    LOCATIONS("Locations", Icons.Filled.LocationOn, Destinations.Locations),
    SETTINGS("Settings", Icons.Filled.Settings, Destinations.Settings)
}

package com.joe.skycast.navigation

import android.app.Activity
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.joe.home.HomeScreen
import com.joe.locations.LocationDetailsScreen
import com.joe.locations.LocationsScreen
import com.joe.models.Place
import com.joe.settings.SettingsScreen
import com.joe.skycast.ui.theme.SkyCastTheme
import kotlin.reflect.typeOf

@Composable
fun SkyCastNavGraph(
    navHostController: NavHostController,
    updateBottomBarState: (Boolean) -> Unit,
    activity : Activity
) {
    NavHost(navController = navHostController, startDestination = Destinations.Home){

        composable<Destinations.Home> {
            updateBottomBarState(true)
            HomeScreen()
        }
        composable<Destinations.Locations> {
            updateBottomBarState(true)
            LocationsScreen(
                onNavigateToDetails = { place ->
                    navHostController.navigate(Destinations.LocationDetails(place))
                },
                activity = activity
            )
        }
        composable<Destinations.Settings> {
            updateBottomBarState(true)
            SettingsScreen()
        }
        composable<Destinations.LocationDetails>(
            typeMap = mapOf(
                typeOf<Place>() to PlaceNavType.placeType
            )
        ) {
            updateBottomBarState(false)
            val args = it.toRoute<Destinations.LocationDetails>()
            LocationDetailsScreen(
                onCancel = {
                    navHostController.navigate(Destinations.Locations)
                },
                place = args.place,
                onAdd = {

                },
            )
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = Color(0xFF132847)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavigation.entries.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == destination.route::class.qualifiedName } == true

            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label,
                        tint = if (isSelected) Color(0xFFFEB800) else Color(0xFF6C788E)
                    )
                },
                label = {
                    Text(
                        text = destination.label,
                        color = if (isSelected) Color(0xFFFEB800) else Color(0xFF6C788E)
                    ) },
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Destinations.Home){
                            inclusive = true
                            saveState = true
                        }
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    SkyCastTheme {
        BottomNavigationBar(rememberNavController())
    }
}
package com.joel.skycast.navigation

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.joel.home.HomeScreen
import com.joel.locations.LocationsScreen
import com.joel.settings.SettingsScreen
import com.joel.skycast.ui.theme.SkyCastTheme

@Composable
fun SkyCastNavGraph(
    navHostController: NavHostController,
) {
    NavHost(navController = navHostController, startDestination = Destinations.Home){
        composable<Destinations.Home> {
            HomeScreen()
        }
        composable<Destinations.Locations> {
            LocationsScreen()
        }
        composable<Destinations.Settings> {
            SettingsScreen()
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        containerColor = Color(0xFF1D1D2A)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavigation.entries.forEach { destination ->
//            val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
            val isSelected = currentDestination?.hierarchy?.any { it.route == destination.route::class.qualifiedName } == true

            NavigationBarItem(
                selected = isSelected,
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label,
                        tint = if (isSelected) Color(0xFF5180f1) else Color.White
                    )
                },
                label = { Text(text = destination.label) },
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
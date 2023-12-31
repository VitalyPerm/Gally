package ru.kvf.gally.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.kvf.design.DesignScreen
import ru.kvf.favorite.FavoriteHost
import ru.kvf.gally.Config
import ru.kvf.photos.navigation.PhotosNavigation
import ru.kvf.settings.SettingsHost

@Composable
fun RootHost(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = RootDestinations.Photos.route
        ) {
            PhotosNavigation(navController, RootDestinations.Photos.route)
            FavoriteHost(navController, RootDestinations.Favorite.route)
            SettingsHost(navController, RootDestinations.Settings.route)
            composable(RootDestinations.Design.route) {
                DesignScreen()
            }
        }
        BottomBar(navController = navController)
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        RootDestinations.getAll().forEach { screen ->
            val shouldDraw = screen !is RootDestinations.Design || Config.DESIGN_SYSTEM
            if (shouldDraw) {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.route,
                            modifier = Modifier
                                .padding(12.dp)
                        )
                    },
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

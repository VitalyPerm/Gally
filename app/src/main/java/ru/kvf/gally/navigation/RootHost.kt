package ru.kvf.gally.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import ru.kvf.core.utils.log
import ru.kvf.design.DesignScreen
import ru.kvf.favorite.ui.navigation.favoriteNavigation
import ru.kvf.gally.BuildConfig
import ru.kvf.photos.navigation.photosNavigation
import ru.kvf.settings.navigation.settingsNavigation

@Composable
fun RootHost(
    navController: NavHostController,
    isScrollInProgress: MutableState<Boolean>,
    vm: RootHostViewModel = koinViewModel()
) {
    val state by vm.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    var navBarVisible by remember {
        mutableStateOf(true)
    }
    val navBarVisibleDestinations = remember {
        RootDestinations.getVisibleNavBarDestinations()
    }
    LaunchedEffect(backStackEntry, isScrollInProgress.value, state.edgeToEdgeEnable) {
        val currentRoute = backStackEntry?.destination?.route
        val scrolling = state.edgeToEdgeEnable && isScrollInProgress.value
        navBarVisible = currentRoute in navBarVisibleDestinations && scrolling.not()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = navController,
            startDestination = RootDestinations.Photos.route
        ) {
            photosNavigation(
                navController = navController,
                route = RootDestinations.Photos.route,
                isScrollInProgress = isScrollInProgress
            )
            favoriteNavigation(navController, RootDestinations.Favorite.route)
            settingsNavigation(navController, RootDestinations.Settings.route)
            composable(RootDestinations.Design.route) {
                DesignScreen()
            }
        }
        AnimatedVisibility(navBarVisible) {
            BottomBar(navController = navController)
        }
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
            val shouldDraw = screen !is RootDestinations.Design || BuildConfig.DEBUG
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

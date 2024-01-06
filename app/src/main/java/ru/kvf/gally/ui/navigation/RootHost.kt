package ru.kvf.gally.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.theme.GallyTheme
import ru.kvf.core.utils.Constants
import ru.kvf.core.widgets.LoadableContent
import ru.kvf.design.DesignScreen
import ru.kvf.favorite.ui.navigation.favoriteNavigation
import ru.kvf.folders.ui.navigation.foldersNavigation
import ru.kvf.gally.BuildConfig
import ru.kvf.photos.ui.navigation.photosNavigation
import ru.kvf.settings.ui.navigation.settingsNavigation

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
    val haptic = LocalHapticFeedback.current

    vm.collectSideEffect {
        when (it) {
            RootHostSideEffect.Vibrate -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    ObserveLifeCycleEvents(onResume = vm::onResume, onPause = vm::onPause)

    LaunchedEffect(backStackEntry, isScrollInProgress.value, state.edgeToEdgeEnable) {
        val currentRoute = backStackEntry?.destination?.route
        val value = currentRoute in navBarVisibleDestinations &&
            (state.edgeToEdgeEnable && isScrollInProgress.value).not()
        if (value) delay(Constants.EDGE_TO_EDGE_DELAY)
        navBarVisible = value
    }

    GallyTheme(
        darkTheme = when (state.theme) {
            ThemeType.System -> isSystemInDarkTheme()
            ThemeType.Light -> false
            ThemeType.Black -> true
        }
    ) {
        LoadableContent(loading = state.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = RootDestinations.Photos.route
                ) {
                    photosNavigation(
                        navController = navController,
                        route = RootDestinations.Photos.route,
                        isScrollInProgress = isScrollInProgress
                    )
                    foldersNavigation(
                        navController = navController,
                        route = RootDestinations.Folders.route,
                        isScrollInProgress = isScrollInProgress
                    )
                    favoriteNavigation(
                        navController = navController,
                        route = RootDestinations.Favorite.route,
                        isScrollInProgress = isScrollInProgress
                    )
                    settingsNavigation(navController, RootDestinations.Settings.route)
                    composable(RootDestinations.Design.route) {
                        DesignScreen()
                    }
                }
                BottomBar(
                    navController = navController,
                    visible = navBarVisible
                )
            }
        }
    }
}

@Composable
private fun ObserveLifeCycleEvents(
    onResume: () -> Unit,
    onPause: () -> Unit
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifeCycleOwner, effect = {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                onPause()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifeCycleOwner.lifecycle.removeObserver(observer)
        }
    })
}

@Composable
private fun BoxScope.BottomBar(
    navController: NavHostController,
    visible: Boolean
) {
    val alpha by animateFloatAsState(targetValue = if (visible)1f else 0f, label = "")
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        modifier = Modifier
            .alpha(alpha)
            .align(Alignment.BottomCenter)
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
                    enabled = visible,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

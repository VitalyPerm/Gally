package ru.kvf.gally.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import kotlinx.coroutines.delay
import ru.kvf.core.widgets.LoadableContent
import ru.kvf.design.DesignUi
import ru.kvf.favorite.ui.FavoriteListUi
import ru.kvf.folders.ui.folderlist.FoldersListUi
import ru.kvf.gally.BuildConfig
import ru.kvf.photos.ui.list.PhotosListUi
import ru.kvf.settings.ui.list.SettingsListUi

private const val NAV_BAR_VISIBILITY_DELAY = 1500L

@Composable
fun HomeUi(
    component: HomeComponent
) {
    val state by component.state.collectAsState()
    val haptic = LocalHapticFeedback.current
    val stackState by component.childStack.subscribeAsState()
    val currentChild = remember(stackState) { stackState.active.instance }
    val navigationBarHeight = remember { mutableIntStateOf(0) }
    val isScrollInProgress = remember { mutableStateOf(false) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    val debug = remember { BuildConfig.DEBUG }
    val ld = LocalDensity.current
    val navBarPadding = remember(navigationBarHeight.intValue) {
        with(ld) {
            navigationBarHeight.intValue.toDp().plus(4.dp)
        }
    }

    LaunchedEffect(Unit) {
        component.sideEffect.collect {
            when (it) {
                RootSideEffect.Vibrate -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    LaunchedEffect(isScrollInProgress.value, state.edgeToEdgeEnable) {
        if (state.edgeToEdgeEnable.not()) return@LaunchedEffect
        val needDelay = bottomBarVisible.not()
        if (needDelay) delay(NAV_BAR_VISIBILITY_DELAY)
        bottomBarVisible = isScrollInProgress.value.not()
    }

    LoadableContent(loading = state.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Children(
                stack = component.childStack,
                animation = stackAnimation(slide())
            ) {
                when (val child = it.instance) {
                    is HomeComponent.Child.Photos -> PhotosListUi(
                        component = child.component,
                        isScrollInProgress = isScrollInProgress
                    )
                    is HomeComponent.Child.Folders -> FoldersListUi(child.component, navBarPadding)
                    is HomeComponent.Child.Favorite -> FavoriteListUi(child.component, navBarPadding)
                    is HomeComponent.Child.Settings -> SettingsListUi(child.component)
                    is HomeComponent.Child.Design -> DesignUi(navBarPadding)
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                AnimatedVisibility(
                    visible = bottomBarVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it }
                    ),
                    exit = slideOutVertically(
                        targetOffsetY = { it }
                    )
                ) {
                    BottomBar(
                        current = currentChild,
                        onPageSelected = component::onPageSelected,
                        navigationBarHeight = navigationBarHeight,
                        debug = debug
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    current: HomeComponent.Child,
    onPageSelected: (HomeComponent.Page) -> Unit,
    navigationBarHeight: MutableState<Int>,
    debug: Boolean
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        modifier = Modifier
            .onSizeChanged { if (navigationBarHeight.value == 0) navigationBarHeight.value = it.height }
    ) {
        NavBarItem(
            icon = Icons.Filled.Photo,
            isSelected = current is HomeComponent.Child.Photos,
            onClick = { onPageSelected(HomeComponent.Page.Photos) }
        )

        NavBarItem(
            icon = Icons.Filled.Folder,
            isSelected = current is HomeComponent.Child.Folders,
            onClick = { onPageSelected(HomeComponent.Page.Folders) }
        )

        NavBarItem(
            icon = Icons.Filled.Favorite,
            isSelected = current is HomeComponent.Child.Favorite,
            onClick = { onPageSelected(HomeComponent.Page.Favorite) }
        )

        NavBarItem(
            icon = Icons.Filled.Settings,
            isSelected = current is HomeComponent.Child.Settings,
            onClick = { onPageSelected(HomeComponent.Page.Settings) }
        )

        if (debug) {
            NavBarItem(
                icon = Icons.Filled.DesignServices,
                isSelected = current is HomeComponent.Child.Design,
                onClick = { onPageSelected(HomeComponent.Page.Design) }
            )
        }
    }
}

@Composable
private fun RowScope.NavBarItem(
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = isSelected,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(12.dp)
            )
        },
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

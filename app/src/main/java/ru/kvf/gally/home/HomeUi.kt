package ru.kvf.gally.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.delay
import ru.kvf.core.utils.Constants
import ru.kvf.feature.design.DesignUi
import ru.kvf.feature.favorite.FavoriteUi
import ru.kvf.feature.folders.FoldersListUi
import ru.kvf.feature.settings.SettingsListUi
import ru.kvf.gally.BuildConfig

@Composable
fun HomeUi(
    component: HomeComponent
) {
    val edgeToEdgeEnable by component.edgeToEdgeEnable.collectAsState()
    val stackState by component.childStack.subscribeAsState()
    val currentChild = remember(stackState) { stackState.active.instance }
    val navigationBarHeight = remember { mutableStateOf(0.dp) }
    val isScrollInProgress = remember { mutableStateOf(false) }
    val editModeEnable = remember { mutableStateOf(false) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    val debug = remember { BuildConfig.DEBUG }

    LaunchedEffect(isScrollInProgress.value, edgeToEdgeEnable) {
        if (edgeToEdgeEnable.not()) return@LaunchedEffect
        val needDelay = bottomBarVisible.not()
        if (needDelay) delay(Constants.NAV_BAR_VISIBILITY_DELAY)
        bottomBarVisible = isScrollInProgress.value.not() && editModeEnable.value.not()
    }

    LaunchedEffect(editModeEnable.value) {
        bottomBarVisible = editModeEnable.value.not()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Children(
            stack = component.childStack,
            animation = stackAnimation(slide(orientation = Orientation.Vertical))
        ) {
            when (val child = it.instance) {
                is HomeComponent.Child.Media -> ru.kvf.feature.media.MediaListUi(
                    component = child.component,
                    isScrollInProgress = isScrollInProgress,
                    selectMediaModeEnable = editModeEnable
                )

                is HomeComponent.Child.Folders -> FoldersListUi(
                    child.component,
                    navigationBarHeight.value
                )

                is HomeComponent.Child.Favorite -> FavoriteUi(
                    component = child.component,
                    navBarPadding = navigationBarHeight.value
                )

                is HomeComponent.Child.Settings -> SettingsListUi(child.component)
                is HomeComponent.Child.Design -> DesignUi(navigationBarHeight.value)
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

@Composable
private fun BottomBar(
    current: HomeComponent.Child,
    onPageSelected: (HomeComponent.Page) -> Unit,
    navigationBarHeight: MutableState<Dp>,
    debug: Boolean
) {
    val ld = LocalDensity.current
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        modifier = Modifier
            .onSizeChanged {
                with(ld) { navigationBarHeight.value = it.height.toDp() }
            }
    ) {
        NavBarItem(
            icon = Icons.Filled.Photo,
            isSelected = current is HomeComponent.Child.Media,
            onClick = { onPageSelected(HomeComponent.Page.Media) }
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

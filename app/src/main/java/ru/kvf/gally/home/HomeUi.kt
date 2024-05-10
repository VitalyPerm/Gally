package ru.kvf.gally.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ru.kvf.core.widgets.GridCountIcon
import ru.kvf.core.widgets.ReverseIcon
import ru.kvf.core.widgets.TrashIcon
import ru.kvf.feature.design.DesignUi
import ru.kvf.feature.favorite.FavoriteUi
import ru.kvf.feature.folders.FoldersListUi
import ru.kvf.feature.media.MediaListUi
import ru.kvf.feature.settings.SettingsListUi
import ru.kvf.gally.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUi(
    component: HomeComponent
) {
    val edgeToEdgeEnable by component.edgeToEdgeEnable.collectAsState()
    val stackState by component.childStack.subscribeAsState()
    val currentChild = remember(stackState) { stackState.active.instance }
    val navigationBarHeight = remember { mutableStateOf(0.dp) }
    val editModeEnable = remember { mutableStateOf(false) }
    var bottomBarVisible by remember { mutableStateOf(true) }
    val debug = remember { BuildConfig.DEBUG }
    val title by component.title.subscribeAsState()

    LaunchedEffect(editModeEnable.value) {
        bottomBarVisible = editModeEnable.value.not()
    }

    val animBarsEnable = remember(editModeEnable, currentChild) {
        edgeToEdgeEnable && currentChild is HomeComponent.Child.Media
    }

    val topBarScrollBehavior =
        if (edgeToEdgeEnable) TopAppBarDefaults.enterAlwaysScrollBehavior() else null
    val bottomBarScrollBehavior =
        if (edgeToEdgeEnable) BottomAppBarDefaults.exitAlwaysScrollBehavior() else null
    val nestedScrollModifier = remember(animBarsEnable) {
        if (topBarScrollBehavior != null && bottomBarScrollBehavior != null && animBarsEnable) {
            Modifier
                .nestedScroll(topBarScrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomBarScrollBehavior.nestedScrollConnection)
        } else {
            Modifier
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .then(nestedScrollModifier)
    ) {
        TopAppBar(
            title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary
            ),
            scrollBehavior = topBarScrollBehavior,
            actions = {
                TitleActions(currentChild)
            }
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Children(
                stack = component.childStack,
                animation = stackAnimation(slide(orientation = Orientation.Vertical))
            ) {
                when (val child = it.instance) {
                    is HomeComponent.Child.Media -> MediaListUi(
                        component = child.component,
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
        }

        BottomBar(
            current = currentChild,
            onPageSelected = component::onPageSelected,
            navigationBarHeight = navigationBarHeight,
            debug = debug,
            visible = bottomBarVisible,
            bottomBarScrollBehavior = bottomBarScrollBehavior
        )
    }
}

@Composable
private fun TitleActions(instance: HomeComponent.Child) {
    when (instance) {
        HomeComponent.Child.Design -> {}
        is HomeComponent.Child.Favorite -> {
            val gridCellsCount by instance.component.gridCellsCount.collectAsState()
            GridCountIcon(count = gridCellsCount, onClick = instance.component::onGridCountClick)
            ReverseIcon(instance.component::onReverseClick)
        }
        is HomeComponent.Child.Folders -> {
            val gridCellsCount by instance.component.gridCellsCount.collectAsState()
            GridCountIcon(count = gridCellsCount, onClick = instance.component::onGridCountClick)
            ReverseIcon(instance.component::onReverseClick)
            TrashIcon(instance.component::onTrashClick)
        }

        is HomeComponent.Child.Media -> {
            val gridCellsCount by instance.component.gridCellsCount.collectAsState()
            GridCountIcon(count = gridCellsCount, onClick = instance.component::onGridCountClick)
            ReverseIcon(instance.component::onReverseClick)
        }

        is HomeComponent.Child.Settings -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomBar(
    current: HomeComponent.Child,
    onPageSelected: (HomeComponent.Page) -> Unit,
    navigationBarHeight: MutableState<Dp>,
    debug: Boolean,
    visible: Boolean,
    bottomBarScrollBehavior: BottomAppBarScrollBehavior?
) {
    val ld = LocalDensity.current
    AnimatedVisibility(visible) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
            modifier = Modifier
                .onSizeChanged {
                    with(ld) { navigationBarHeight.value = it.height.toDp() }
                },
            scrollBehavior = bottomBarScrollBehavior
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
            )
        },
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

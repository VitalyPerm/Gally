@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
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
    val childStack by component.childStack.collectAsState()
    val animatedTopBar by component.animatedTopBar.collectAsState()
    val animatedBottomBar by component.animatedBottomBar.collectAsState()
    val title by component.title.collectAsState()

    val currentChild = remember(childStack) { childStack.active.instance }
    val editModeEnable = remember { mutableStateOf(false) }
    val debug = remember { BuildConfig.DEBUG }

    val topBarScrollBehavior =
        if (animatedTopBar) TopAppBarDefaults.enterAlwaysScrollBehavior() else null
    val bottomBarScrollBehavior =
        if (animatedBottomBar) BottomAppBarDefaults.exitAlwaysScrollBehavior() else null

    val topBarNestedScroll = remember(topBarScrollBehavior) {
        if (topBarScrollBehavior == null) {
            Modifier
        } else {
            Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
        }
    }

    val bottomBarNestedScroll = remember(bottomBarScrollBehavior) {
        if (bottomBarScrollBehavior == null) {
            Modifier
        } else {
            Modifier.nestedScroll(bottomBarScrollBehavior.nestedScrollConnection)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .then(topBarNestedScroll)
            .then(bottomBarNestedScroll)
    ) {
        TopBar(
            title = title,
            scrollBehavior = topBarScrollBehavior,
            currentChild = currentChild
        )

        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            Children(
                stack = childStack,
                animation = stackAnimation(slide(orientation = Orientation.Horizontal))
            ) {
                when (val child = it.instance) {
                    is HomeComponent.Child.Media -> MediaListUi(
                        component = child.component,
                        selectMediaModeEnable = editModeEnable
                    )

                    is HomeComponent.Child.Folders -> FoldersListUi(child.component)

                    is HomeComponent.Child.Favorite -> FavoriteUi(
                        component = child.component,
                    )

                    is HomeComponent.Child.Settings -> SettingsListUi(child.component)
                    is HomeComponent.Child.Design -> DesignUi()
                }
            }
        }

        BottomBar(
            current = currentChild,
            onPageSelected = component::onPageSelected,
            debug = debug,
            editModeEnable = editModeEnable.value,
            bottomBarScrollBehavior = bottomBarScrollBehavior
        )
    }
}

@Composable
fun TopBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior?,
    currentChild: HomeComponent.Child
) {
    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.inversePrimary
        ),
        scrollBehavior = scrollBehavior,
        actions = {
            TitleActions(currentChild)
        }
    )
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
    debug: Boolean,
    editModeEnable: Boolean,
    bottomBarScrollBehavior: BottomAppBarScrollBehavior?
) {
    AnimatedVisibility(!editModeEnable) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.inversePrimary,
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

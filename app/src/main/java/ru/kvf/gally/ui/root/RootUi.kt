package ru.kvf.gally.ui.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.theme.GallyTheme
import ru.kvf.gally.ui.home.HomeUi
import ru.kvf.photos.ui.detail.PhotoUi
import ru.kvf.photos.ui.list.PhotosListUi

@Composable
fun RootUi(
    component: RootComponent
) {
    val theme by component.theme.collectAsState()

    GallyTheme(
        darkTheme = when (theme) {
            ThemeType.System -> isSystemInDarkTheme()
            ThemeType.Light -> false
            ThemeType.Black -> true
        }
    ) {
        Children(
            stack = component.childStack,
            animation = stackAnimation(slide())
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeUi(component = child.component)
                is RootComponent.Child.Photo -> PhotoUi(component = child.component)
                is RootComponent.Child.FolderPhotosList -> PhotosListUi(component = child.component)
            }
        }
    }
}

package ru.kvf.gally.ui.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.theme.GallyTheme
import ru.kvf.gally.ui.home.HomeUi
import ru.kvf.media.ui.detail.MediaUi
import ru.kvf.media.ui.list.MediaListUi

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
                is RootComponent.Child.Media -> MediaUi(component = child.component)
                is RootComponent.Child.FolderMediaList -> MediaListUi(component = child.component)
            }
        }
    }
}

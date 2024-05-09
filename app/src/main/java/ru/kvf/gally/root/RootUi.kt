package ru.kvf.gally.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ru.kvf.core.domain.entities.ThemeType
import ru.kvf.core.theme.GallyTheme
import ru.kvf.feature.media.MediaListUi
import ru.kvf.feature.trash.TrashUi
import ru.kvf.gally.home.HomeUi

@Composable
fun RootUi(
    component: RootComponent
) {
    val theme by component.theme.collectAsState()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        component.sideEffect.collect {
            when (it) {
                RootComponent.SideEffect.Vibrate ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }

    GallyTheme(
        darkTheme = when (theme) {
            ThemeType.System -> isSystemInDarkTheme()
            ThemeType.Light -> false
            ThemeType.Black -> true
        }
    ) {
        Children(
            stack = component.childStack,
            animation = stackAnimation { child ->
                when (child.instance) {
                    is RootComponent.Child.FolderMediaList -> localScale()
                    is RootComponent.Child.Home -> scale()
                    is RootComponent.Child.Trash -> localScale()
                }
            }
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeUi(component = child.component)
                is RootComponent.Child.FolderMediaList -> MediaListUi(component = child.component)
                is RootComponent.Child.Trash -> TrashUi(component = child.component)
            }
        }
    }
}

private fun localScale() = scale(
    frontFactor = 1.5f,
    backFactor = 0.7f
)

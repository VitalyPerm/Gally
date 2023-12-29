package ru.kvf.gally.ui.navigation

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.ThemeType

@Stable
data class RootHostState(
    val edgeToEdgeEnable: Boolean = false,
    val theme: ThemeType = ThemeType.System,
    val loading: Boolean = true
)

sealed interface RootHostSideEffect {
    data object Vibrate : RootHostSideEffect
}

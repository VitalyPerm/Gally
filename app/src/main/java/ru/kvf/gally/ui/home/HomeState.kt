package ru.kvf.gally.ui.home

import androidx.compose.runtime.Stable

@Stable
data class HomeState(
    val edgeToEdgeEnable: Boolean = false,
    val loading: Boolean = true
)

sealed interface RootSideEffect {
    data object Vibrate : RootSideEffect
}

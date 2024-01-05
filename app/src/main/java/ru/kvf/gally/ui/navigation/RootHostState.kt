package ru.kvf.gally.ui.navigation

data class RootHostState(
    val edgeToEdgeEnable: Boolean = false,
    val loading: Boolean = true
)

sealed interface RootHostSideEffect {
    data object Vibrate : RootHostSideEffect
}

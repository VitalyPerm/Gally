package ru.kvf.gally.ui.navigation

data class RootHostState(
    val edgeToEdgeEnable: Boolean = false
)

sealed interface RootHostSideEffect

package ru.kvf.gally.navigation

data class RootHostState(
    val edgeToEdgeEnable: Boolean = false
)

sealed interface RootHostSideEffect

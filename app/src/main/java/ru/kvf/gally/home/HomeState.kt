package ru.kvf.gally.home

import androidx.compose.runtime.Stable

@Stable
data class HomeState(
    val edgeToEdgeEnable: Boolean = false,
    val loading: Boolean = true
)

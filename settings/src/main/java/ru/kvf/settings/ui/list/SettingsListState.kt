package ru.kvf.settings.ui.list

import ru.kvf.core.domain.entities.ThemeType

data class SettingsListState(
    val edgeToEdge: Boolean = true,
    val theme: ThemeType = ThemeType.System,
)

sealed interface SettingsListSideEffect

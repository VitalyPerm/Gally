package ru.kvf.settings.ui.list

import ru.kvf.core.domain.entities.ThemeType
import java.util.Calendar

data class SettingsListState(
    val edgeToEdge: Boolean = true,
    val theme: ThemeType = ThemeType.System,
    val sortBy: Int = Calendar.DAY_OF_YEAR
)

sealed interface SettingsListSideEffect

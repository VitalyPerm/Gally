package ru.kvf.settings.ui.list

import ru.kvf.core.domain.entities.Setting
import ru.kvf.core.domain.entities.ThemeType

data class SettingsListState(
    val settings: List<Pair<Setting, Boolean>> = Setting.getAll().map { it to false },
    val theme: ThemeType = ThemeType.System,
    val loading: Boolean = true
)

sealed interface SettingsListSideEffect

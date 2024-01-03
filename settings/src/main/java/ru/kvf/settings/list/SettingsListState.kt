package ru.kvf.settings.list

import ru.kvf.core.domain.entities.Setting

data class SettingsListState(
    val settings: List<Pair<Setting, Boolean>> = Setting.getAll().map { it to false },
    val loading: Boolean = true
)

sealed interface SettingsListSideEffect

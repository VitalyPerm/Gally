package ru.kvf.settings.ui.list

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.ThemeType

interface SettingsListComponent {
    val state: StateFlow<SettingsListState>

    fun onThemeChanged(themeType: ThemeType)
    fun onEdgeToEdgeChanged(enable: Boolean)
    fun onSortByChanged(value: Int)
}

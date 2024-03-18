package ru.kvf.settings.ui.list

import kotlinx.coroutines.flow.StateFlow
import ru.kvf.core.domain.entities.ThemeType

interface SettingsListComponent {
    val edgeToEdgeEnable: StateFlow<Boolean>
    val theme: StateFlow<ThemeType>
    val sortBy: StateFlow<Int>

    fun onThemeChanged(themeType: ThemeType)
    fun onEdgeToEdgeChanged(enable: Boolean)
    fun onSortByChanged(value: Int)
}

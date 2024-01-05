package ru.kvf.settings.ui.navigation

sealed class SettingsDestinations(val route: String) {
    data object List : SettingsDestinations("settings_list")
}

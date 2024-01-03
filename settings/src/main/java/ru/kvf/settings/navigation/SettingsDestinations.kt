package ru.kvf.settings.navigation

sealed class SettingsDestinations(val route: String) {
    data object List : SettingsDestinations("settings_list")
}

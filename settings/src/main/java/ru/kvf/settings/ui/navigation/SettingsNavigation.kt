package ru.kvf.settings.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kvf.settings.ui.list.SettingsListScreen

fun NavGraphBuilder.settingsNavigation(
    navController: NavController,
    route: String
) {
    navigation(startDestination = SettingsDestinations.List.route, route = route) {
        composable(SettingsDestinations.List.route) {
            SettingsListScreen()
        }
    }
}

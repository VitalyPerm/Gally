package ru.kvf.gally.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kvf.gally.core.widgets.StubScreen

fun NavGraphBuilder.SettingsHost(navController: NavHostController, route: String) {
    navigation(startDestination = "set", route = route) {
        composable("set") {
            StubScreen(text = "Settings", modifier = Modifier.clickable { navController.navigate("set2") })
        }

        composable("set2") {
            StubScreen(text = "Settings2")
        }
    }
}

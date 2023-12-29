package ru.kvf.gally.feature.favorite

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kvf.gally.core.widgets.StubScreen

fun NavGraphBuilder.FavoriteHost(navController: NavHostController, route: String) {
    navigation(startDestination = "fav", route = route) {
        composable("fav") {
            StubScreen(text = "Favortite", modifier = Modifier.clickable { navController.navigate("fav2") })
        }

        composable("fav2") {
            StubScreen(text = "Favortite2")
        }
    }
}

package ru.kvf.gally.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import ru.kvf.gally.ui.navigation.RootHost

class MainActivity : ComponentActivity() {

    companion object {
        private const val READ_PHOTO_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
        }

        setContent {
            val navController = rememberNavController()
            val isScrollInProgress = remember { mutableStateOf(false) }
            RootHost(
                navController = navController,
                isScrollInProgress = isScrollInProgress
            )
        }

        val hasPhotoPermission = checkSelfPermission(READ_PHOTO_PERMISSION) == PackageManager.PERMISSION_GRANTED

        if (hasPhotoPermission.not()) {
            permissionLauncher.launch(READ_PHOTO_PERMISSION)
        }
    }
}

package ru.kvf.gally

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import ru.kvf.core.theme.GallyTheme
import ru.kvf.gally.navigation.RootHost

class MainActivity : ComponentActivity() {

    companion object {
        private const val READ_PHOTO_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
        }

        setContent {
            val navController = rememberNavController()
            GallyTheme {
                RootHost(navController)
            }
        }

        val hasPhotoPermission = checkSelfPermission(READ_PHOTO_PERMISSION) == PackageManager.PERMISSION_GRANTED

        if (hasPhotoPermission.not()) {
            permissionLauncher.launch(READ_PHOTO_PERMISSION)
        }
    }
}

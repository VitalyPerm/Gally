package ru.kvf.gally.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import ru.kvf.core.ComponentFactory
import ru.kvf.core.koin
import ru.kvf.core.widgets.GrantPermissionScreen
import ru.kvf.gally.createRootComponent
import ru.kvf.gally.ui.root.RootUi

class MainActivity : ComponentActivity() {

    companion object {
        private const val READ_PHOTO_PERMISSION = Manifest.permission.READ_MEDIA_IMAGES
        private const val READ_VIDEO_PERMISSION = Manifest.permission.READ_MEDIA_VIDEO
    }
    private var permissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result -> permissionGranted = result.values.all { true } }

        permissionGranted = checkSelfPermission(READ_PHOTO_PERMISSION) == PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(READ_VIDEO_PERMISSION) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted.not()) {
            permissionLauncher.launch(arrayOf(READ_VIDEO_PERMISSION, READ_PHOTO_PERMISSION))
        }
        val componentFactory = application.koin.get<ComponentFactory>()
        val rootComponent = componentFactory.createRootComponent(defaultComponentContext())

        setContent {
            if (permissionGranted) {
                RootUi(component = rootComponent)
            } else {
                GrantPermissionScreen(::openSettings)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        permissionGranted = checkSelfPermission(READ_PHOTO_PERMISSION) == PackageManager.PERMISSION_GRANTED
    }

    private fun openSettings() {
        Intent().apply {
            data = Uri.fromParts("package", packageName, null)
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        }.also(::startActivity)
    }
}

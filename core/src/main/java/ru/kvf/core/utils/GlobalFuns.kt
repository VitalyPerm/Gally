package ru.kvf.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.IntentSenderRequest
import androidx.core.app.ShareCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.getMimeType
import java.util.Calendar

fun Int?.toCalendarSort(): Int = if (this == 2) Calendar.MONTH else Calendar.DAY_OF_YEAR

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.enableFullScreen() {
    findActivity()?.let { activity ->
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.statusBars())
        insetsController.hide(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Context.disableFullScreen() {
    findActivity()?.let { activity ->
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        val insetsController = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
        insetsController.show(WindowInsetsCompat.Type.statusBars())
        insetsController.show(WindowInsetsCompat.Type.navigationBars())
    }
}

fun Context.shareMedia(mediaList: List<Media>) {
    if (mediaList.isEmpty()) return
    val intent = ShareCompat
        .IntentBuilder(this)
        .setType(mediaList.getMimeType())

    mediaList.forEach {
        intent.addStream(it.uri)
    }
    intent.startChooser()
}

fun Context.createTrashMediaRequest(uris: List<Uri>): IntentSenderRequest {
    val intent = MediaStore.createTrashRequest(
        contentResolver,
        uris,
        true
    )
    return IntentSenderRequest.Builder(intent)
        .setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION, 0)
        .build()
}

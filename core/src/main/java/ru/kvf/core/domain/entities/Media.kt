package ru.kvf.core.domain.entities

import android.net.Uri
import androidx.compose.runtime.Immutable

@Immutable
data class Media(
    val id: Long,
    val name: String,
    val uri: Uri,
    val folder: String,
    val timeStamp: Long,
    val date: MediaDate = MediaDate.empty,
    val mimeType: MimeType,
    val duration: String?
)

fun List<Media>.getMimeType(): String {
    val hasPhoto = any { it.mimeType == MimeType.Image }
    val hasVideo = any { it.mimeType == MimeType.Video }
    return when {
        hasPhoto && hasVideo -> "image/*, video/*"
        hasPhoto -> "image/*"
        else -> "video/*"
    }
}

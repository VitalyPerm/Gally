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
    val duration: String?
)

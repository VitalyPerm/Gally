package ru.kvf.core.domain.entities

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class Photo(
    val id: Long,
    val name: String,
    val uri: Uri,
    val folder: String,
    val timeStamp: Long,
    val date: PhotoDate = PhotoDate.empty
)

package ru.kvf.core.domain.entities

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Photo(
    val id: Long,
    val name: String,
    val uri: Uri,
    val folder: String,
    val timeStamp: Long,
    val date: PhotoDate = PhotoDate.empty
)

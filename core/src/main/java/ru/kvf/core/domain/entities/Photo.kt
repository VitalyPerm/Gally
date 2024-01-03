package ru.kvf.core.domain.entities

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Photo(
    val id: Long,
    val name: String,
    val date: PhotoDate,
    val uri: Uri,
    val folder: String
)

package ru.kvf.gally.feature.photos.domain

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class Photo(
    val id: Long,
    val name: String,
    val date: String,
    val uri: Uri,
    val folder: String
)

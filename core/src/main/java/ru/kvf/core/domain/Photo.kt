package ru.kvf.core.domain

import android.net.Uri
import androidx.compose.runtime.Stable
import ru.kvf.core.data.CustomDate

@Stable
data class Photo(
    val id: Long,
    val name: String,
    val date: CustomDate,
    val uri: Uri,
    val folder: String
)

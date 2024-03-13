package ru.kvf.media.ui.detail

import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Media

@Stable
data class MediaState(
    val media: List<Media> = emptyList(),
    val currentIndex: Int? = null,
    val titleVisible: Boolean = false,
    val title: String = "",
    val reversed: Boolean = false
)

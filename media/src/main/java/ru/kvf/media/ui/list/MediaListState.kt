package ru.kvf.media.ui.list

import android.net.Uri
import androidx.compose.runtime.Stable
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Stable
data class MediaListState(
    val media: Map<MediaDate, List<Media>> = emptyMap(),
    val reversedMedia: Map<MediaDate, List<Media>> = emptyMap(),
    val likedMedia: List<Long> = emptyList(),
    val reversed: Boolean = false,
    val gridCellsCount: Int = 1,
    val folderName: String? = null,
    val lastPosition: Int = 0,
    val deleteMediaCandidate: Media? = null
)

sealed interface MediaListSideEffect {
    data object ScrollUp : MediaListSideEffect
    data class DeleteMedia(val uri: Uri) : MediaListSideEffect
}

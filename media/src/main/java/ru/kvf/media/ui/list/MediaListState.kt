package ru.kvf.media.ui.list

import android.net.Uri
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.persistentSetOf
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Stable
data class MediaListState(
    val media: ImmutableMap<MediaDate, List<Media>> = persistentMapOf(),
    val reversedMedia: ImmutableMap<MediaDate, List<Media>> = persistentMapOf(),
    val likedMedia: ImmutableList<Long> = persistentListOf(),
    val reversed: Boolean = false,
    val gridCellsCount: Int = 1,
    val folderName: String? = null,
    val lastPosition: Int = 0,
    val selectedMediaIds: ImmutableSet<Long> = persistentSetOf(),
    val mediaToTrashUris: ImmutableList<Uri> = persistentListOf()
) {
    val selectMediaModeEnable get() = selectedMediaIds.isNotEmpty()
}

sealed interface MediaListSideEffect {
    data object ScrollUp : MediaListSideEffect
    data class DeleteMedia(val uris: List<Uri>) : MediaListSideEffect
    data class ShareMedia(val media: List<Media>) : MediaListSideEffect
}

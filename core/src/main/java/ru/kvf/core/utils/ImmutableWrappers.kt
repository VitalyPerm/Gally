package ru.kvf.core.utils

import android.net.Uri
import androidx.compose.runtime.Immutable
import ru.kvf.core.domain.entities.Folder
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Immutable
@JvmInline
value class MediaMap(val data: Map<MediaDate, List<Media>>) {
    companion object {
        val EMPTY = MediaMap(emptyMap())
        fun from(data: Map<MediaDate, List<Media>>) = MediaMap(data)
    }
}

@Immutable
@JvmInline
value class UriSet(val data: Set<Uri>) {
    companion object {
        val EMPTY = UriSet(emptySet())
        fun from(data: Set<Uri>) = UriSet(data)
    }
}

@Immutable
@JvmInline
value class LongSet(val data: Set<Long>) {
    fun toMutableSet() = data.toMutableSet()
    companion object {
        val EMPTY = LongSet(emptySet())
        fun from(data: Set<Long>) = LongSet(data)
    }
}

@Immutable
@JvmInline
value class MediaDateSet(val data: Set<MediaDate>) {
    companion object {
        val EMPTY = MediaDateSet(emptySet())
        fun from(data: Set<MediaDate>) = MediaDateSet(data)
    }
}

@Immutable
@JvmInline
value class FolderList(val data: List<Folder>) {
    fun reversed() = FolderList(data.reversed())

    companion object {
        val EMPTY = FolderList(emptyList())
    }
}

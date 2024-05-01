package ru.kvf.core.utils

import android.net.Uri
import androidx.compose.runtime.Immutable
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

@Immutable
data class MediaMap(val data: Map<MediaDate, List<Media>>) {
    companion object {
        val EMPTY = MediaMap(emptyMap())
        fun from(data: Map<MediaDate, List<Media>>) = MediaMap(data)
    }
}

@Immutable
data class UriSet(val data: Set<Uri>) {
    companion object {
        val EMPTY = UriSet(emptySet())
        fun from(data: Set<Uri>) = UriSet(data)
    }
}

@Immutable
data class LongSet(val data: Set<Long>) {
    fun toMutableSet() = data.toMutableSet()
    companion object {
        val EMPTY = LongSet(emptySet())
        fun from(data: Set<Long>) = LongSet(data)
    }
}

@Immutable
data class MediaDateSet(val data: Set<MediaDate>) {
    companion object {
        val EMPTY = MediaDateSet(emptySet())
        fun from(data: Set<MediaDate>) = MediaDateSet(data)
    }
}

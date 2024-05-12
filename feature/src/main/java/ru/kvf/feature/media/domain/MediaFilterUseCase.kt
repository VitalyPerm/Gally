package ru.kvf.feature.media.domain

import kotlinx.coroutines.flow.Flow

interface MediaFilterUseCase {
    fun get(): Flow<Pair<Boolean, Boolean>>
    fun set(filter: MediaFilter)
}

enum class MediaFilter { Photo, Video }

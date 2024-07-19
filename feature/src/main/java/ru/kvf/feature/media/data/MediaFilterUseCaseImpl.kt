package ru.kvf.feature.media.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.kvf.feature.media.domain.MediaFilter
import ru.kvf.feature.media.domain.MediaFilterUseCase

class MediaFilterUseCaseImpl : MediaFilterUseCase {
    private companion object {
        const val DEBOUNCE = 1000L
    }

    private var lastSetVideo = 0L
    private var lastSetPhoto = 0L

    private val flow = MutableStateFlow(MediaFilter())

    override fun get(): Flow<MediaFilter> = flow

    override fun changeVideo() {
        val now = System.currentTimeMillis()
        if ((now - lastSetVideo) < DEBOUNCE) return
        flow.update { lastData ->
            lastData.copy(video = !lastData.video)
        }
        lastSetVideo = now
    }

    override fun changePhoto() {
        val now = System.currentTimeMillis()
        if ((now - lastSetPhoto) < DEBOUNCE) return
        flow.update { lastData ->
            lastData.copy(photo = !lastData.photo)
        }
        lastSetPhoto = now
    }
}

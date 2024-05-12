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

    private var lastSet = 0L

    private val flow = MutableStateFlow(true to true)

    override fun get(): Flow<Pair<Boolean, Boolean>> = flow

    override fun set(filter: MediaFilter) {
        val now = System.currentTimeMillis()
        if ((now - lastSet) < DEBOUNCE) return
        when (filter) {
            MediaFilter.Video -> flow.update { it.copy(second = !it.second) }
            MediaFilter.Photo -> flow.update { it.copy(first = !it.first) }
        }
        lastSet = now
    }
}

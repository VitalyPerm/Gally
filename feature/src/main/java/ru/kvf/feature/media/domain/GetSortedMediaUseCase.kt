package ru.kvf.feature.media.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Media
import ru.kvf.core.domain.entities.MediaDate

interface GetSortedMediaUseCase {
    operator fun invoke(): Flow<Map<MediaDate, List<Media>>>
}

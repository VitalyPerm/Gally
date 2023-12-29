package ru.kvf.photos.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo
import ru.kvf.core.domain.entities.PhotoDate

interface GetSortedPhotosUseCase {
    operator fun invoke(): Flow<Map<PhotoDate, List<Photo>>>
}

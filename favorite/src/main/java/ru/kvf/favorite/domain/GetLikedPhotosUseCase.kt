package ru.kvf.favorite.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.entities.Photo

interface GetLikedPhotosUseCase {
    operator fun invoke(): Flow<List<Photo>>
}
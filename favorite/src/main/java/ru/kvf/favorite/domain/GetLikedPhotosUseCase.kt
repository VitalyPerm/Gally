package ru.kvf.favorite.domain

import kotlinx.coroutines.flow.Flow
import ru.kvf.core.domain.Photo

interface GetLikedPhotosUseCase {
    fun get(): Flow<List<Photo>>
}